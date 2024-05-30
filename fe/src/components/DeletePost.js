import React from 'react';

const DeletePost = ({postId}) => {
  const deletePost = async () => {
    try {
      const response = await fetch(`http://localhost:8080/post/delete/${postId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type' : 'application/json',
        }
      });
      if(response.ok){
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');
        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
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