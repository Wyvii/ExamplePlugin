package me.ashydev.exampleplugin.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.ashydev.exampleplugin.user.BukkitUser;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.UUID;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

// This is terrible the way of doing this but im kind of lazy rn, would be much better to serialize this using a more abstract approach with
// all the custom serializers build for data, but im going to do this like this, because as I said im quite lazy right now, and this project
// is fairly simple
public class ExampleDatabase implements Database<UUID, BukkitUser> {
    private final String url;
    private MongoDatabase database;
    private MongoClient client;
    private MongoCollection<Document> users;

    public ExampleDatabase(String url) {
        this.url = url;
    }

    @Override
    public void connect() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new ConnectionString(url))
                .build();

        client = MongoClients.create(settings);

        database = client.getDatabase("data");
        users = database.getCollection("users");

        initialize();

    }

    @Override
    public void initialize() { }

    @Override
    public BukkitUser load(UUID id) {
        final Document document = users.find(new Document("id", id)).first();

        if (document == null) {
            final BukkitUser user = BukkitUser.allocate(id);
            final Document doc = convert(user);

            users.insertOne(doc);

            return user;
        }

        return new BukkitUser(
                document.get("id", UUID.class),
                document.getDouble("gold"),
                document.getInteger("kills"),
                document.getInteger("deaths")
        );
    }

    @Override
    public void save(BukkitUser user) {
        final Document document = convert(user);

        users.replaceOne(new Document("id", user.getUniqueId()), document);
    }

    private Document convert(BukkitUser user) {
        return new Document("id", user.getUniqueId())
                .append("gold", user.getGold())
                .append("kills", user.getKills())
                .append("deaths", user.getDeaths());
    }
}
