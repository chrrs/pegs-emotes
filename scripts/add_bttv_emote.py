import datetime
import hashlib
import json
import requests
import sys

file = "../content/v2/emotes.json"

emotes = {}
with open(file) as f:
    emotes = json.load(f)

if "emotes" not in emotes:
    emotes["emotes"] = []

for id in sys.argv[1:]:
    res = requests.get(f"https://api.betterttv.net/3/emotes/{id}").json()

    if "message" in res:
        print(f"could not add {id}: " + res["message"])
        continue

    url = f"https://cdn.betterttv.net/emote/{res['id']}/2x.{res['imageType']}"
    image_res = requests.get(url)
    sha1 = hashlib.sha1(image_res.content).digest().hex()

    emotes["emotes"].append({
        "name": res["code"],
        "format": res["imageType"],
        "url": url,
        "sha1": sha1,
    })

    print(f"added {res['code']} ({id}, {res['imageType']})")

emotes["emotes"].sort(key=lambda emote: emote["name"].lower())

with open(file, "w") as f:
    emotes["lastUpdated"] = datetime.datetime.now().isoformat()
    json.dump(emotes, f, indent=4)
