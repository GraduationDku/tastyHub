import React, { useState, useEffect } from "react";

function Post({ setScreen, onPostSelect }) {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    async function fetchAllPost() {
      try {
        const response = await fetch('http://localhost:8080/post/list', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setPosts(data); // data.content가 undefined일 경우 빈 배열로 설정
        }
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    }
    fetchAllPost();
  }, []);

  return (
    <>
      <div>재료 공유 게시글 조회</div>
      <button onClick={() => setScreen('createpost')}>게시글 작성하기</button>
      <ul>
        {posts.map(post => (
          <li key={post.postId} onClick={() => onPostSelect(post.postId)}>
            <h3>{post.title}</h3>
            <div>
              <p>{post.userImg || '정보 없음'}</p>
              <p>{post.nickname || '정보 없음'}</p>
              <p>{post.postState || '정보 없음'}</p>
            </div>
          </li>
        ))}
      </ul>
    </>
  );
}

export default Post;
