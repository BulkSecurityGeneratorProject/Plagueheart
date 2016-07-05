package au.com.iglooit.searchcloud.config.elasticsearch;

import org.elasticsearch.client.Client;

/**
 * Created by nicholaszhu on 2/07/2016.
 */
public class ElasticSearchWrapper {
    private Client client;

    public ElasticSearchWrapper(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
