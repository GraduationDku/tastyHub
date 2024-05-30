import React from "react";

const DeleteChatroom = ({roomId}) => {
  const deleteChatroom = async () => {
    try {
      const response = await fetch(`http://localhost:8080/chatroom/${roomId}`,{
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      if (response.ok){
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');
        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
        alert('Chatroom deleted successfully!');
      }else {
        throw new Error('Failed to delete chatroom');
      }
    }catch (error){
    console.error('Error deleting chatroom:', error);
    alert('Error deleting chatroom');
  }
};

return(
  <button onClick={deleteChatroom}> 채팅방 삭제하기 </button>
)
}