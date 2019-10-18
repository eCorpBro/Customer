package com.github.ecorpbro.database;

public class SettingsDbSchema {
    public static final class SourceUrlTable {
        public static final String DB_TABLE_NAME = "sourceurl";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String URL = "url";
        }
    }

    public static final class DefaultUrlTable {
        public static final String DB_TABLE_NAME = "defaulturl";

        public static final class Cols {
            public static final String URL = "url";
        }
    }

}
