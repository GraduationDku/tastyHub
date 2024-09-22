package com.example.tastyhub.common.domain.chat.service;

import static com.example.tastyhub.common.domain.userChat.entity.QUserChatRoom.userChatRoom;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.chat.dtos.CheckDto;
import com.example.tastyhub.common.domain.chat.entity.Chat;
import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import com.example.tastyhub.common.domain.chat.repository.ChatRepository;
import com.example.tastyhub.common.domain.chat.repository.ChatRoomRepository;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.service.PostServiceImpl;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userChat.entity.UserChatRoom;
import com.example.tastyhub.common.domain.userChat.repository.UserChatRoomRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatRepository chatRepository;
  private final UserChatRoomRepository userChatRoomRepository;
  private final PostServiceImpl postService;

  @Override
  @Transactional
  public void createChatRoom(Long postId, User user) {
    PostResponse post = postService.getPost(postId);
    if (!post.getNickname().equals(user.getNickname())) {
      throw new IllegalArgumentException("해당 유저는 채팅을 만들 수 없습니다.");
    }
    ChatRoom chatRoom = ChatRoom.builder().postId(postId).chatRoomTitle(post.getTitle()).build();
    chatRoomRepository.save(chatRoom);

    UserChatRoom userChatRoom = UserChatRoom.builder().user(user).chatRoom(chatRoom).build();
    userChatRoomRepository.save(userChatRoom);
  }

  @Override
  @Transactional
  public Page<ChatRoomDto> getChatRoomList(User user, Pageable pageable) {
    List<UserChatRoom> userChatRooms = userChatRoomRepository.findByUser(user);
    List<ChatRoomDto> collect = userChatRooms.stream()
        .map(ChatRoomDto::new)
        .collect(Collectors.toList());
    return convertListToPage(collect, pageable);
  }

  @Override
  @Transactional
  public List<ChatDto> getChatRoom(Long roomId, User user) {
    ChatRoom chatRoom = findChatRoomById(roomId);

    if(userChatRoomRepository.existsByChatRoomAndUser(chatRoom,user)){
      List<Chat> chats = chatRoom.getChats();
      return chats.stream().map(ChatDto::new).collect(Collectors.toList());
    }else{
      UserChatRoom userChatRoom = UserChatRoom.builder().user(user).chatRoom(chatRoom).build();
      userChatRoomRepository.save(userChatRoom);
      List<Chat> chats = chatRoom.getChats();
      return chats.stream().map(ChatDto::new).collect(Collectors.toList());
    }

  }

  @Override
  @Transactional
  public void enterNewChatRoom(Long roomId, User user1) {
    ChatRoom chatRoom = findChatRoomById(roomId);
    boolean check = isCheck(user1, chatRoom);

    if (check){
      throw new IllegalArgumentException("해당 유저는 이미 채팅방에 존재합니다.");
    }
    UserChatRoom userChatRoom = UserChatRoom.builder().user(user1).chatRoom(chatRoom).build();
    userChatRoomRepository.save(userChatRoom);
  }

  private static boolean isCheck(User user1, ChatRoom chatRoom) {
    return chatRoom.getUserChatRooms().stream().map(UserChatRoom::getUser)
        .noneMatch(user -> user.equals(user1));
  }

  @Override
  @Transactional
  public void outChatRoom(Long roomId, User user) {
    ChatRoom chatRoom = findChatRoomById(roomId);
    UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저는 채팅방에 존재하지 않습니다."));
    userChatRoomRepository.delete(userChatRoom);
  }

  @Override
  @Transactional
  public void deleteChatRoom(Long roomId, Long postId, User user) {
    PostResponse post = postService.getPost(postId);
    if (!post.getNickname().equals(user.getNickname())) {
      throw new IllegalArgumentException("해당 유저는 채팅방을 삭제할 수 없습니다.");
    }
    ChatRoom chatRoom = findChatRoomById(roomId);
    if (userChatRoomRepository.existsByChatRoomAndUser(chatRoom, user)) {
      chatRoomRepository.delete(chatRoom);
    } else {
      throw new IllegalArgumentException("해당 유저는 채팅방에 존재하지 않습니다.");
    }
  }

  @Override
  @Transactional
  public CheckDto checkRoomCondition(Long postId) {
    if (!chatRoomRepository.existsByPostId(postId)) {
      return CheckDto.builder().checkRoom(false).build();
    }
    return CheckDto.builder().checkRoom(true).build();
  }

  private ChatRoom findChatRoomById(Long roomId) {
    return chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("현재 채팅방은 존재하지 않습니다."));
  }


  // 기존 리스트를 페이지로 변환하는 메서드
  public static <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), list.size());

    // start가 리스트 크기를 초과하는 경우 빈 리스트 반환
    if (start > list.size()) {
      return new PageImpl<>(List.of(), pageable, list.size());
    }

    List<T> pageContent = list.subList(start, end);

    return new PageImpl<>(pageContent, pageable, list.size());
  }

  // 임의의 ChatRoomDto 리스트 생성 메서드
  private static List<ChatRoomDto> generateChatRoomList() {
    return IntStream.range(1, 51)
        .mapToObj(i -> new ChatRoomDto((long) i, "ChatRoom " + i))
        .collect(Collectors.toList());
  }
}


