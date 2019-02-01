package com.wowsanta.scim.schema;

public class SCIMDefinitions {
    public static enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, BINARY, REFERENCE, COMPLEX
    }
    public static enum Mutability {
        READ_WRITE, READ_ONLY, IMMUTABLE, WRITE_ONLY
    }
    public static enum Returned {
        ALWAYS, NEVER, DEFAULT, REQUEST
    }
    public static enum Uniqueness {
        NONE, SERVER, GLOBAL
    }
    public static enum ReferenceType {
        USER, GROUP, EXTERNAL, URI
    }
    public static enum ResoureType {
        USER, GROUP, Schema,ResourceType;
    }
}
