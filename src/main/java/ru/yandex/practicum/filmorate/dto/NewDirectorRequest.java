package ru.yandex.practicum.filmorate.dto;

import lombok.Generated;

public class NewDirectorRequest extends BaseDirectorRequest {
    private String name;

    public NewDirectorRequest() {
    }

    public NewDirectorRequest(String name) {
        this.name = name;
    }

    @Generated
    public static NewDirectorRequestBuilder builder() {
        return new NewDirectorRequestBuilder();
    }

    @Generated
    public NewDirectorRequestBuilder toBuilder() {
        return (new NewDirectorRequestBuilder()).name(this.name);
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setName(final String name) {
        this.name = name;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NewDirectorRequest)) {
            return false;
        } else {
            NewDirectorRequest other = (NewDirectorRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof NewDirectorRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "NewDirectorRequest(name=" + this.getName() + ")";
    }

    @Generated
    public static class NewDirectorRequestBuilder {
        @Generated
        private String name;

        @Generated
        NewDirectorRequestBuilder() {
        }

        @Generated
        public NewDirectorRequestBuilder name(final String name) {
            this.name = name;
            return this;
        }

        @Generated
        public NewDirectorRequest build() {
            return new NewDirectorRequest(this.name);
        }

        @Generated
        public String toString() {
            return "NewDirectorRequest.NewDirectorRequestBuilder(name=" + this.name + ")";
        }
    }
}

