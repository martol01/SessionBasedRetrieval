print("<parameter>")
print("<stopper>")

with open("stopwords.txt", "r") as f:
    for line in f:
        cleanedLine = line.strip()
        if cleanedLine: 
        	print("<word>" + cleanedLine + "</word>")
            
print("</stopper>")
print("</parameter>")
