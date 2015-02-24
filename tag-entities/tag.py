import requests
from xml.etree import ElementTree
from xml.etree.ElementTree import Element

url = "http://localhost:8080/dexter-webapp/api/rest/annotate"

tree = ElementTree.parse("data.xml")
root = tree.getroot()

for i, interaction in enumerate(root.iter("interaction")):
    top_5_individual = []
    for snippet in interaction.iter("snippet"):
        if snippet.text is not None:
            params = {
                "text": snippet.text,
                "n": 50,
                "wn": True,
                "debug": False,
                "format": "text",
                "min-conf": 0.5
            }
            r = requests.get(url, params=params)
            entities = r.json()["spots"]
            counts = {}
            for entity in entities:
                entity_id = entity["entity"]
                count = counts.get(entity_id, {}).get("count") or 0
                counts[entity_id] = {
                    "count": count + 1,
                    "text": entity["mention"]
                }
            counts = [(k, v["count"], v["text"]) for k, v in counts.iteritems()]
            sorted_counts = sorted(counts, key=lambda t: t[1])
            sorted_counts.reverse()
            top_5 = sorted_counts[:5]
            top_5_individual.append(top_5)

    # Andy says it's cryptic Haskell neckebeard code, but it actually just flattens the list
    counts = {}
    for entity in sum(top_5_individual, []):
        if counts.get(entity[0]):
            existing_entity = counts[entity[0]]
            counts[entity[0]] = (existing_entity[0], existing_entity[1] + entity[1], existing_entity[2])
        else:
            counts[entity[0]] = entity
    top_5_individual = [v for k, v in counts.iteritems()]
    top_5 = sorted(top_5_individual, key=lambda t: t[1])
    top_5.reverse()
    top_5 = top_5[:5]

    entities_elem = Element("entities")
    for entity in top_5:
        e = Element("entity")
        e.text = entity[2]
        entities_elem.append(e)
    interaction.append(entities_elem)

    print i
    if i == 5:
        break

tree.write("data2.xml")
