import datetime
import hashlib
import json
import requests
import sys
import os

path = "../content/v2/"
emotes_file = os.path.join(path, "emotes.json")
local_dir = os.path.join(path, "downloaded")

emotes = {}
with open(emotes_file) as f:
    emotes = json.load(f)

def download(emote):
    name = emote["name"]
    url = emote["url"]
    imageType = emote["format"]

    if not url.startswith("http"):
        print(f"skipped {name}")
        return emote

    image_res = requests.get(url)
    sha1 = hashlib.sha1(image_res.content).digest().hex()

    fileName = f"{name}.{imageType}"
    with open(os.path.join(local_dir, fileName), "wb") as f:
        f.write(image_res.content)

    emote["url"] = f"downloaded/{fileName}"
    emote["sha1"] = sha1

    print(f"downloaded {name}")
    return emote

if not os.path.isdir(local_dir):
    os.makedirs(local_dir)

emotes["emotes"] = [download(emote) for emote in emotes["emotes"]]
emotes["emotes"].sort(key=lambda emote: emote["name"].lower())

with open(emotes_file, "w") as f:
    emotes["lastUpdated"] = datetime.datetime.now().isoformat()
    json.dump(emotes, f, indent=4)
