package me.chrrrs.pegsemotes.emotes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class EmoteRegistry {
    private final Logger LOGGER = LogManager.getLogger("pegs-emotes.emotes.EmoteRegistry");

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
            registerEmote(new Emote("5Head", "5head.png"));
            registerEmote(new Emote("Bedge", "bedge.png"));
            registerEmote(new Emote("Clueless", "clueless.png"));
            registerEmote(new Emote("EZ", "ez.png"));
            registerEmote(new Emote("Hmm", "hmm.png"));
            registerEmote(new Emote("HUHH", "huhh.png"));
            registerEmote(new Emote("HYPERS", "hypers.png"));
            registerEmote(new Emote("KEKW", "kekw.png"));
            registerEmote(new Emote("Madge", "madge.png"));
            registerEmote(new Emote("monkaHmm", "monka-hmm.png"));
            registerEmote(new Emote("monkaW", "monkaw.png"));
            registerEmote(new Emote("monkaS", "monkas.png"));
            registerEmote(new Emote("Okayge", "okayge.png"));
            registerEmote(new Emote("OMEGALUL", "omegalul.png"));
            registerEmote(new Emote("peepoHappy", "peepo-happy.png"));
            registerEmote(new Emote("PepeHands", "pepe-hands.png"));
            registerEmote(new Emote("peepoSit", "peepo-sit.png"));
            registerEmote(new Emote("peepoSmile", "peepo-smile.png"));
            registerEmote(new Emote("POGGIES", "poggies.png"));
            registerEmote(new Emote("PogU", "pogu.png"));
            registerEmote(new Emote("Prayge", "prayge.png"));
            registerEmote(new Emote("Sadge", "sadge.png"));
            registerEmote(new Emote("widepeepoHappy", "wide-peepo-happy.png"));
            registerEmote(new Emote("WICKED", "wicked.png"));
            registerEmote(new Emote("YEP", "yep.png"));

            // Animated
            // Use: https://sheeptester.github.io/words-go-here/misc/animated-painting-maker.html & save image from it for the GIF
            // For frame time: https://ezgif.com/maker (Frames - Multiply delay value by 10 for milliseconds) EG. peepo-leave = Delay = 2 x10 = frameTimeMs = 20
            registerEmote(new Emote("Aware", "animated/aware.png", 50));
            registerEmote(new Emote("Awkward", "animated/awkward.png", 100));
            registerEmote(new Emote("catJAM", "animated/catjam.png", 40));
            registerEmote(new Emote("catKISS", "animated/catkiss.png", 70));
            registerEmote(new Emote("Chatting", "animated/chatting.png", 20));
            registerEmote(new Emote("Clap", "animated/clap.png", 200));
            registerEmote(new Emote("COPIUM", "animated/copium.png", 20));
            registerEmote(new Emote("DinkDonk", "animated/dink-donk.png", 50));
            registerEmote(new Emote("DonoWall", "animated/donowall.png", 20));
            registerEmote(new Emote("Drake", "animated/drake.png", 70));
            registerEmote(new Emote("GIGACHAD", "animated/gigachad.png", 40));
            registerEmote(new Emote("HACKERMANS", "animated/hackermans.png", 50));
            registerEmote(new Emote("LETSGO", "animated/letsgo.png", 30));
            registerEmote(new Emote("modCheck", "animated/modcheck.png", 90));
            registerEmote(new Emote("NODDERS", "animated/nodders.png", 20));
            registerEmote(new Emote("NOOO", "animated/nooo.png", 30));
            registerEmote(new Emote("NOPERS", "animated/nopers.png", 70));
            registerEmote(new Emote("NOTED", "animated/noted.png", 20));
            registerEmote(new Emote("peepoArrive", "animated/peepo-arrive.png", 100));
            registerEmote(new Emote("peepoComfy", "animated/peepo-comfy.png", 100));
            registerEmote(new Emote("PeepoGiggles", "animated/peepo-giggles.png", 30));
            registerEmote(new Emote("peepoRun", "animated/peepo-run.png", 40));
            registerEmote(new Emote("peepoShy", "animated/peepo-shy.png", 250));
            registerEmote(new Emote("peepoLeave", "animated/peepo-leave.png", 100));
            registerEmote(new Emote("peepoWow", "animated/peepo-wow.png", 100));
            registerEmote(new Emote("ppOverheat", "animated/ppoverheat.png", 20));
            registerEmote(new Emote("ppPoof", "animated/pp-poof.png", 30));
            registerEmote(new Emote("TeaTime", "animated/teatime.png", 120));
            registerEmote(new Emote("ThisIsFine", "animated/thisisfine.png", 100));
            registerEmote(new Emote("trevorHug", "animated/trevorhug.png", 100));
            registerEmote(new Emote("VIBE", "amimated/vibe.png", 40));
            registerEmote(new Emote("WAYTOODANK", "animated/waytoodank.png", 20));
            registerEmote(new Emote("YesYes", "animated/yesyes.png", 100));
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
}
