package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.chat.entity.Chat;
import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import com.example.tastyhub.common.domain.chat.repository.ChatRepository;
import com.example.tastyhub.common.domain.chat.repository.ChatRoomRepository;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.service.PostServiceImpl;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final PostServiceImpl postService;

    @Override
    public void createChatRoom(Long postId, User user) {
        PostResponse post = postService.getPost(postId);
        if (!post.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("해당 유저는 채팅을 만들 수 없습니다.");
        }
        ChatRoom chatRoom = ChatRoom.builder().postId(postId).chatRoomTitle(post.getTitle()).build();
        addUserInRoom(user, chatRoom);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoomDto> getChatRoomList(User user) {
        List<ChatRoomDto> chatRoomDtoList = chatRoomRepository.findAllByUserQuery(user);
        return chatRoomDtoList;
    }

    @Override
    public List<ChatDto> getChatRoom(Long roomId, User user) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        if(!chatRoom.getUsers().contains(user)){
            throw new IllegalArgumentException("해당 유저는 접근할 권리가 없습니다.");
        }
        List<Chat> chats = chatRoom.getChats();
        List<ChatDto> getChatRoom = chats.stream().map(ChatDto::new).collect(Collectors.toList());
        return getChatRoom;
    }

    @Override
    public void enterNewChatRoom(Long roomId, User user) {
        ChatRoom chatRoomById = findChatRoomById(roomId);
        addUserInRoom(user, chatRoomById);
    }

    @Override
    public void outChatRoom(Long roomId, User user) {
        ChatRoom chatRoomById = findChatRoomById(roomId);
        List<User> users = chatRoomById.getUsers();
        users.remove(user);
        chatRoomById.updateUser(users);
    }

    @Override
    public void deleteChatRoom(Long roomId, Long postId, User user) {
        PostResponse post = postService.getPost(postId);
        if (!post.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("해당 유저는 채팅방을 삭제할 수 없습니다.");
        }
        ChatRoom chatRoomById = findChatRoomById(roomId);
        if (!chatRoomById.getUsers().contains(user)) {
            throw new IllegalArgumentException("해당 유저는 채팅방에 존재하지 않습니다.");
        }
        chatRepository.deleteById(roomId);
    }

    private ChatRoom findChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("현재 채팅방은 존재하지 않습니다."));
    }

    private static void addUserInRoom(User user, ChatRoom chatRoomById) {
        List<User> users = chatRoomById.getUsers();
        users.add(user);
        chatRoomById.updateUser(users);
    }
}
