package lol.oce.tntrun.tntrun.players;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lol.oce.tntrun.tntrun.TNTRun;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Getter
public class PlayerManager {

    private final List<TNTPlayer> players = new ArrayList<>();
    private final MongoCollection<Document> playerCollection;

    public PlayerManager() {
        String mongoUrl = TNTRun.get().getConfigManager().getSettingsFile().getConfiguration().getString("mongo.url");
        MongoClient mongoClient = MongoClients.create(mongoUrl);
        MongoDatabase database = mongoClient.getDatabase("tntrun");
        playerCollection = database.getCollection("players");
    }

    public void addPlayer(TNTPlayer player) {
        players.add(player);
        savePlayer(player);
    }

    public void removePlayer(TNTPlayer player) {
        players.remove(player);
    }

    public TNTPlayer getPlayer(UUID uuid) {
        return players.stream().filter(player -> player.getPlayer().getUniqueId().equals(uuid)).findFirst().orElseGet(() -> loadPlayer(uuid));
    }

    private void savePlayer(TNTPlayer player) {
        Document doc = new Document("uuid", player.getPlayer().getUniqueId().toString())
                .append("gamesPlayed", player.getGamesPlayed())
                .append("gamesWon", player.getGamesWon())
                .append("gamesLost", player.getGamesLost())
                .append("inGame", player.isInGame())
                .append("match", player.getMatch() != null ? player.getMatch().getUuid().toString() : null);
        Bson filter = eq("uuid", player.getPlayer().getUniqueId().toString());
        playerCollection.replaceOne(filter, doc, new ReplaceOptions().upsert(true));
    }

    private TNTPlayer loadPlayer(UUID uuid) {
        Document doc = playerCollection.find(eq("uuid", uuid.toString())).first();
        if (doc != null) {
            TNTPlayer player = new TNTPlayer(
                    TNTRun.get().getServer().getPlayer(uuid),
                    doc.getInteger("gamesPlayed"),
                    doc.getInteger("gamesWon"),
                    doc.getInteger("gamesLost"),
                    doc.getBoolean("inGame"),
                    null // Load match if necessary
            );
            players.add(player);
            return player;
        }
        return null;
    }
}