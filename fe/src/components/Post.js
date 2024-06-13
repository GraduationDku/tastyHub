import React, { useState, useEffect } from "react";
import '../css/Post.css';

function Post({ setScreen, onPostSelect, isGuest }) {
  const [posts, setPosts] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(new Set());

  useEffect(() => {
    if (isGuest) {
      setScreen('signup');
      return;
    }

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
          setPosts(data);
        }
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    }
    fetchAllPost();
  }, [isGuest, setScreen]);

  const handleDeleteModeToggle = () => {
    setDeleteMode(!deleteMode);
    setSelectedPosts(new Set());
  };

  const handleCheckboxChange = (postId) => {
    const updatedSelectedPosts = new Set(selectedPosts);
    if (updatedSelectedPosts.has(postId)) {
      updatedSelectedPosts.delete(postId);
    } else {
      updatedSelectedPosts.add(postId);
    }
    setSelectedPosts(updatedSelectedPosts);
  };

  const handleDeleteSelected = async () => {
    for (let postId of selectedPosts) {
      try {
        const response = await fetch(`http://localhost:8080/post/delete/${postId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (!response.ok) {
          throw new Error(`Failed to delete post with id ${postId}`);
        }
      } catch (error) {
        console.error('Error deleting post:', error);
      }
    }
    setPosts(posts.filter(post => !selectedPosts.has(post.postId)));
    setSelectedPosts(new Set());
    setDeleteMode(false);
  };

  return (
    <>
      <div className="post">
        <h1>재료 공유 게시글 조회</h1>
        <div className="box">
          <button onClick={() => setScreen('createpost')}>게시글 작성하기</button>
          <button onClick={handleDeleteModeToggle}>
            {deleteMode ? '취소' : '삭제하기'}
          </button>
          {deleteMode && (
            <button onClick={handleDeleteSelected} disabled={selectedPosts.size === 0}>
              선택된 게시글 삭제
            </button>
          )}
          <ul>
            {posts.map(post => (
              <li key={post.postId}>
                <div className="seperate">
                  {deleteMode && (
                    <input
                      type="checkbox"
                      checked={selectedPosts.has(post.postId)}
                      onChange={() => handleCheckboxChange(post.postId)}
                    />
                  )}
                  <h3 onClick={() => !deleteMode && onPostSelect(post.postId)}>
                    {post.title}
                  </h3>
                  <div className="seperate">
                    <p>{post.userImg || '정보 없음'}</p>
                    <p>{post.nickname || '정보 없음'}</p>
                    <p>{post.postState || '정보 없음'}</p>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </>
  );
}

export default Post;
