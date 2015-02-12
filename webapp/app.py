import web
import json

urls = (
    '/search', 'search'
)


class search:
    def GET(self):
        f = open("mock_results.json", "r")
        json_str = f.read()
        f.close()
        results = json.loads(json_str)
        return web.template.render("templates").search(results)

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
