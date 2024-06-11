import React, { useState, useEffect } from "react";
import Comment from "./Comment";

function PostDetails({ postId, setScreen }) {
  const [postDetails, setPostDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [roomExists, setRoomExists] = useState(false);

  const fetchPostDetails = async () => {
    try {
      const response = await fetch(`http://localhost:8080/post/detail/${postId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        }
      });
      if (response.ok) {
        const data = await response.json();
        setPostDetails(data);
      } else {
        throw new Error('Failed to fetch post details');
      }
    } catch (error) {
      console.error('Error fetching post details:', error);
    } finally {
      setLoading(false);
    }
  };

  const checkRoomExists = async () => {
    try {
      const response = await fetch(`http://localhost:8080/room/check/${postId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        }
      });
      if (response.ok) {
        const data = await response.json();
        setRoomExists(data.checkRoom);
      } else {
        throw new Error('Failed to check room existence');
      }
    } catch (error) {
      console.error('Error checking room existence:', error);
    }
  };

  const enterChatRoom = async () => {
    if (!roomExists) return;

    try {
      const response = await fetch(`http://localhost:8080/room/${postId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        }
      });
      if (response.ok) {
        const data = await response.json();
        // Process the chat data as needed
        console.log(data.chatDtoList);
        setScreen('sendchat'); // Assuming sendchat is the screen for the chat room
      } else {
        throw new Error('Failed to enter chat room');
      }
    } catch (error) {
      console.error('Error entering chat room:', error);
    }
  };

  useEffect(() => {
    if (postId) {
      fetchPostDetails();
      checkRoomExists();
    }
  }, [postId]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!postDetails) {
    return <div>No post details available</div>;
  }

  return (
    <div className="post-details">
      <h1>{postDetails.title}</h1>
      <p>{postDetails.text}</p>
      <p>{postDetails.postState}</p>
      <p>{postDetails.nickname}</p>
      <p>{postDetails.latestUpdateTime}</p>
      
      <Comment postId={postId} refreshComments={fetchPostDetails} comments={postDetails.commentDtos} />
      
      <button onClick={() => setScreen('post')}>목록으로 돌아가기</button>
      <button
        onClick={enterChatRoom}
        disabled={!roomExists}
        style={{
          backgroundColor: roomExists ? 'blue' : 'gray',
          cursor: roomExists ? 'pointer' : 'not-allowed'
        }}
      >
        {roomExists ? '채팅방 입장' : '채팅방 없음'}
      </button>
    </div>
  );
}

export default PostDetails;
