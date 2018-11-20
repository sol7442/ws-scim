package com.wowsanta.scim.attribute;

import com.wowsanta.scim.schema.SCIMDefinitions;

@SuppressWarnings("serial")
public abstract class AbstractAttribute implements Attribute {
    protected String uri;
    protected String name;
    protected SCIMDefinitions.DataType type = null;
    protected Boolean multiValued;
    protected String description;
    protected Boolean required;
    protected Boolean caseExact;
    protected SCIMDefinitions.Mutability mutability;
    protected SCIMDefinitions.Returned returned;
    protected SCIMDefinitions.Uniqueness uniqueness;

    public String getURI() {
        return uri; }

    public void setURI(String uri) {
        this.uri = uri; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SCIMDefinitions.DataType getType() {
        return type;
    }

    public void setType(SCIMDefinitions.DataType type) {
        this.type = type;
    }

    public Boolean getMultiValued() {
        return multiValued;
    }

    public void setMultiValued(Boolean multiValued) {
        this.multiValued = multiValued;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getCaseExact() {
        return caseExact;
    }

    public void setCaseExact(Boolean caseExact) {
        this.caseExact = caseExact;
    }

    public SCIMDefinitions.Mutability getMutability() {
        return mutability;
    }

    public void setMutability(SCIMDefinitions.Mutability mutability) {
        this.mutability = mutability;
    }

    public SCIMDefinitions.Returned getReturned() {
        return returned; }

    public void setReturned(SCIMDefinitions.Returned returned) {
        this.returned = returned;
    }

    public SCIMDefinitions.Uniqueness getUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(SCIMDefinitions.Uniqueness uniqueness) {
        this.uniqueness = uniqueness;
    }

}
