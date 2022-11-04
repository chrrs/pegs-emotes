package uk.co.algid.fabricemotes.emotes;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EmoteRegistry {
    private final Logger LOGGER = LogManager.getLogger("fabricemotes.emotes.EmoteRegistry");

    private static EmoteRegistry emoteRegistry;

    public ConcurrentHashMap<Integer, Emote> emoteIdMap;
    public ConcurrentHashMap<String, Emote> emoteMap;

    private EmoteRegistry() {}

    public static EmoteRegistry getInstance() {
        if (emoteRegistry == null) {
            emoteRegistry = new EmoteRegistry();
        }

        return emoteRegistry;
    }

    public void init() {
        emoteIdMap = new ConcurrentHashMap<>();
        emoteMap = new ConcurrentHashMap<>();

        try {
            registerEmote(new Emote("Bedge", "bedge.png"));
            registerEmote(new Emote("bright_eyes", "bright-eyes.png"));
            registerEmote(new Emote("catCry", "cat-cry.png"));
            registerEmote(new Emote("catScream", "cat-scream.png"));
            registerEmote(new Emote("cryIgnore", "cry-ignore.png"));
            registerEmote(new Emote("ded", "ded.png"));
            registerEmote(new Emote("ggeeAww", "ggee-aww.png"));
            registerEmote(new Emote("ggeeCoffee", "ggee-coffee.png"));
            registerEmote(new Emote("ggeeFacePalm", "ggee-face-palm.png"));
            registerEmote(new Emote("ggeeHaha", "ggee-haha.png"));
            registerEmote(new Emote("ggeeHello", "ggee-hello.png"));
            registerEmote(new Emote("ggeeHmm", "ggee-hmm.png"));
            registerEmote(new Emote("ggeeOmg", "ggee-omg.png"));
            registerEmote(new Emote("ggeeOOOO", "ggee-oooo.png"));
            registerEmote(new Emote("ggeeSleep", "ggee-sleep.png"));
            registerEmote(new Emote("ggeeTeeHee", "ggee-tee-hee.png"));
            registerEmote(new Emote("ggeeWhycry", "ggee-whycry.png"));
            registerEmote(new Emote("ggeeWut", "ggee-wut.png"));
            registerEmote(new Emote("Hmm", "hmm.png"));
            registerEmote(new Emote("Hmmge", "hmmge.png"));
            registerEmote(new Emote("KEKW", "kekw.png"));
            registerEmote(new Emote("Madge", "madge.png"));
            registerEmote(new Emote("monkaS", "monkas.png"));
            registerEmote(new Emote("NEways", "neways.png"));
            registerEmote(new Emote("noSanity", "no-sanity.png"));
            registerEmote(new Emote("Okayge", "okayge.png"));
            registerEmote(new Emote("peepoBlush", "peepo-blush.png"));
            registerEmote(new Emote("peepoHappy", "peepo-happy.png"));
            registerEmote(new Emote("peepoHappyGun", "peepo-happy-gun.png"));
            registerEmote(new Emote("peepoPoint", "peepo-point.png"));
            registerEmote(new Emote("peepoSelfie", "peepo-selfie.png"));
            registerEmote(new Emote("PepeHands", "pepe-hands.png"));
            registerEmote(new Emote("PepeNotes", "pepe-notes.png"));
            registerEmote(new Emote("POGGIES", "poggies.png"));
            registerEmote(new Emote("Prayge", "prayge.png"));
            registerEmote(new Emote("Sadge", "sadge.png"));
            registerEmote(new Emote("Smadge", "smadge.png"));
            registerEmote(new Emote("Starege", "starege.png"));
            registerEmote(new Emote("suffer", "suffer.png"));
            registerEmote(new Emote("Susge", "susge.png"));
            registerEmote(new Emote("YesHoney", "yes-honey.png"));

            // Animated
            // Use: https://sheeptester.github.io/words-go-here/misc/animated-painting-maker.html
            // For frame time: https://ezgif.com/maker (Frames - multiply delay value by 10 for MS)
            registerEmote(new Emote("catJAM", "animated/catjam.png", 40));
            registerEmote(new Emote("HYPERJAMMIES", "animated/hyperjammies.png", 20));
            registerEmote(new Emote("HYPERS", "animated/hypers.png", 70));
            registerEmote(new Emote("Jammies", "animated/jammies.png", 90));
            registerEmote(new Emote("MONKE", "animated/monke.png", 30));
            registerEmote(new Emote("NODDERS", "animated/nodders.png", 20));
            registerEmote(new Emote("NOPERS", "animated/nopers.png", 70));
            registerEmote(new Emote("peepoShy", "animated/peepo-shy.png", 250));
            registerEmote(new Emote("Tastyge", "animated/tastyge.png", 30));
        } catch (Exception e) {
            LOGGER.error("init(): Failed to load emotes", e);
        }
    }

    public void registerEmote(Emote emote) throws Exception {
        if (emoteMap.containsKey(emote.getName())) {
            throw new Exception("Emote with the name " + emote.getName() + " already exists, failed to register");
        }

        emoteIdMap.put(emote.getId(), emote);
        emoteMap.put(emote.getName(), emote);
    }

    public Emote getEmoteById(int id) {
        return emoteIdMap.get(id);
    }

    public Emote getEmoteByName(String name) {
        return emoteMap.get(name);
    }

    public Collection<String> getEmoteSuggestions() {
        return Lists.newArrayList(emoteMap.keys().asIterator()).stream().map((name) -> ":" + name + ":").collect(Collectors.toList());
    }
}
