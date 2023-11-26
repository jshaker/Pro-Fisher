package Runner;

import com.epicbot.api.shared.model.Area;

import java.util.HashMap;

public class Configuration {

    public String name;
    public BankConfiguration bank_config;

    public EscapeConfiguration escape_configuration;

    public Area area_of_interest;

    public int default_delay;

    public HashMap<String, Integer> must_have;

    public GetEntity get_entity;
}

