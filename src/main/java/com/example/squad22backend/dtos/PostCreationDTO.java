package com.example.squad22backend.dtos;

public record PostCreationDTO(String title, String content, Integer likes, String visibility, String communityId, String communityPostStatus) {
}
