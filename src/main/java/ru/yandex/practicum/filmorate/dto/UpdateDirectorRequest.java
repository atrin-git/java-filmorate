package ru.yandex.practicum.filmorate.dto;

import lombok.Generated;

public class UpdateDirectorRequest extends BaseDirectorRequest {
    private Long id;
    private String name;

    public boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    @Generated
    UpdateDirectorRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Generated
    public static UpdateDirectorRequestBuilder builder() {
        return new UpdateDirectorRequestBuilder();
    }

    @Generated
    public UpdateDirectorRequestBuilder toBuilder() {
        return (new UpdateDirectorRequestBuilder()).id(this.id).name(this.name);
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setId(final Long id) {
        this.id = id;
    }

    @Generated
    public void setName(final String name) {
        this.name = name;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UpdateDirectorRequest)) {
            return false;
        } else {
            UpdateDirectorRequest other = (UpdateDirectorRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

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
        return other instanceof UpdateDirectorRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getId();
        return "UpdateDirectorRequest(id=" + var10000 + ", name=" + this.getName() + ")";
    }

    @Generated
    public static class UpdateDirectorRequestBuilder {
        @Generated
        private Long id;
        @Generated
        private String name;

        @Generated
        UpdateDirectorRequestBuilder() {
        }

        @Generated
        public UpdateDirectorRequestBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public UpdateDirectorRequestBuilder name(final String name) {
            this.name = name;
            return this;
        }

        @Generated
        public UpdateDirectorRequest build() {
            return new UpdateDirectorRequest(this.id, this.name);
        }

        @Generated
        public String toString() {
            return "UpdateDirectorRequest.UpdateDirectorRequestBuilder(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}

