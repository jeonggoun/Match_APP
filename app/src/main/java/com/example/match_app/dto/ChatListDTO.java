package com.example.match_app.dto;

public class ChatListDTO {
    MetaDTO metaDTO;
    ChattingDTO chattingDTO;

    public MetaDTO getMetaDTO() {
        return metaDTO;
    }

    public void setMetaDTO(MetaDTO metaDTO) {
        this.metaDTO = metaDTO;
    }

    public ChattingDTO getChattingDTO() {
        return chattingDTO;
    }

    public void setChattingDTO(ChattingDTO chattingDTO) {
        this.chattingDTO = chattingDTO;
    }
}
