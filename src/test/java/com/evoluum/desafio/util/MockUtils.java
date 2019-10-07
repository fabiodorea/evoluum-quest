package com.evoluum.desafio.util;

import com.evoluum.desafio.domain.Estado;
import com.evoluum.desafio.domain.views.EstadoResponse;
import com.evoluum.desafio.domain.views.MunicipioResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class MockUtils {

    public static List<Estado> findAllStatesFromIbge() throws IOException, URISyntaxException {
        URL path = ResourceUtils.getURL("src/test/resources/fixture/all_states_before.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path.toURI()), new TypeReference<List<Estado>>(){});
    }

    public static List<EstadoResponse> findAllStates() throws IOException, URISyntaxException {
        URL path = ResourceUtils.getURL("src/test/resources/fixture/all_states_after.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path.toURI()), new TypeReference<List<EstadoResponse>>(){});
    }

    public static List<MunicipioResponse> findCountysByUf_28() throws IOException, URISyntaxException {
        URL path = ResourceUtils.getURL("src/test/resources/fixture/municipios_by_uf_28.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path.toURI()), new TypeReference<List<MunicipioResponse>>(){});
    }
}
