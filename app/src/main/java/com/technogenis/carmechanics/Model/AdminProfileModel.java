package com.technogenis.carmechanics.Model;

public class AdminProfileModel
{
    String fullName;
    String bio;
    String experience;
    String coverImageLink;
    String profileImageLink;

    public AdminProfileModel(String fullName, String bio, String experience, String coverImageLink, String profileImageLink) {
        this.fullName = fullName;
        this.bio = bio;
        this.experience = experience;
        this.coverImageLink = coverImageLink;
        this.profileImageLink = profileImageLink;
    }

    public AdminProfileModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCoverImageLink() {
        return coverImageLink;
    }

    public void setCoverImageLink(String coverImageLink) {
        this.coverImageLink = coverImageLink;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }
}
