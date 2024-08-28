import React from "react";

const DeleteChatroom = ({roomId}) => {
  const deleteChatroom = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/chatroom/${roomId}`,{
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization' : localStorage.getItem('accessToken')
        }
      });
      if (response.ok){
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