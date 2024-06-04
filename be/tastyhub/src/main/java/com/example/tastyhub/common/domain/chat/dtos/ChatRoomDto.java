package com.example.tastyhub.common.domain.chat.dtos;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.userChat.entity.UserChatRoom;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private Long roomId;
    private String chatRoomTitle;

    public ChatRoomDto(UserChatRoom userChatRoom){
        this.roomId = userChatRoom.getChatRoom().getId();
        this.chatRoomTitle = userChatRoom.getChatRoom().getChatRoomTitle();
    }

}
