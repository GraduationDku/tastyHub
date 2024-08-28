import React from 'react';

const DeletePost = ({postId}) => {
  const deletePost = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/post/delete/${postId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type' : 'application/json',
          'Authorization' : localStorage.getItem('accessToken')
        }
      });
      if(response.ok){
        alert('Post deleted successfully!');
      } else{
        throw new Error('Failed to delete post');
      }
    }catch (error){
      console.error('Error deleting post');
      alert('Error deleting post');
    }
  };

  return (
    <button onClick={deletePost}>게시물 삭제하기</button>
  )
}
export default DeletePost;