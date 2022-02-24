public final class OutcastModeration extends JavaPlugin {
    static OutcastModeration instance = null;

    public OutcastCore coreAPI = null;
    public OutcastRanks rankAPI = null;
    public CreateConfig config, punishData, staff;

    @Override
    public void onEnable() {
        instance = this;
        coreAPI = (OutcastCore) Bukkit.getPluginManager().getPlugin("OutcastCore");
        rankAPI = (OutcastRanks) Bukkit.getPluginManager().getPlugin("OutcastRanks");

        if (coreAPI == null) {
            new LogMe("Unable to find OutcastCore!").Error();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (rankAPI == null) {
            new LogMe("Unable to find OutcastRanks!").Error();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        new LogMe("Moderation starting...").Warning();

        config = new CreateConfig("config.yml", "Outcast/Moderation");
        punishData = new CreateConfig("punishments.yml", "Outcast/Moderation");
        staff = new CreateConfig("staff.yml", "Outcast/Moderation");

        new ConfigState(config).configDefaults();
        new ConfigState(punishData).loadConfig();
        new ConfigState(staff).loadStaffConfig();

        setupCommands();
        setupListeners();
    }

    @Override
    public void onDisable() {
        new LogMe("Moderation stopping...").Warning();

        new ConfigState(punishData).saveConfig();
    }

    public static OutcastModeration getInstance() {
        return instance;
    }

    private void setupCommands() {
        getCommand("om").setExecutor(new Reload());

        getCommand("mute").setExecutor(new Mute());
        getCommand("m").setExecutor(new Mute());

        getCommand("ban").setExecutor(new Ban());
        getCommand("b").setExecutor(new Ban());

        getCommand("freeze").setExecutor(new Freeze());
        getCommand("f").setExecutor(new Freeze());

        getCommand("invsee").setExecutor(new Invsee());
        getCommand("is").setExecutor(new Invsee());

        getCommand("kick").setExecutor(new Kick());
        getCommand("k").setExecutor(new Kick());

        getCommand("nick").setExecutor(new Nick());
        getCommand("staff").setExecutor(new Staff());
        getCommand("tp").setExecutor(new Tp());

        getCommand("vanish").setExecutor(new Vanish());
        getCommand("v").setExecutor(new Vanish());

        getCommand("warn").setExecutor(new Warn());
        getCommand("w").setExecutor(new Warn());
    }

    private void setupListeners() {
        String packageName = getClass().getPackage().getName();
        for (Class<?> cl : new Reflections(packageName + ".Events").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) cl.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
