package com.jana.citysearch.repositories;

import android.content.Context;

import com.jana.citysearch.R;
import com.jana.citysearch.model.City;
import com.silverforge.elasticsearchrawclient.connector.ConnectorSettings;
import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticClient;
import com.silverforge.elasticsearchrawclient.elasticFacade.ElasticRawClient;
import com.silverforge.elasticsearchrawclient.utils.StreamUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.net.URISyntaxException;

import rx.Observable;

@EBean
public class CityRepository implements Repository<City> {

    private ElasticRawClient client;

    @RootContext
    public Context context;

    public CityRepository () {
        try {
            ConnectorSettings settings = ConnectorSettings
                    .builder()
                    .indices(new String[] {"cities"})
                    .types(new String[] {"city"})
                    .baseUrl("https://mgj.east-us.azr.facetflow.io:443")
                    .userName("wihIilbbekmCeppKlgQXDwpSZEUekkk0")
                    .build();

            client = new ElasticClient(settings);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<City> searchAsync(String text) {
        String queryTemplate = StreamUtils.getRawContent(context, R.raw.search_query);

        String query = queryTemplate
                .replace("{{SIZE}}", "1000")
                .replace("{{NAME}}", text);

        return client.searchAsync(query, City.class);
    }
}
