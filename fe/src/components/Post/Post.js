// src/components/Post/Post.js
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { fetchPosts, deletePosts } from '../../redux/postState';
import '../../css/Post/Post.css';

function Post({ setScreen, onPostSelect, isGuest }) {
  const dispatch = useDispatch();
  const { posts, loading, error } = useSelector((state) => state.post);
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(new Set());

  useEffect(() => {
    if (isGuest) {
      setScreen('signup');
      return;
    }

    // 게시글 가져오기
    dispatch(fetchPosts());
  }, [dispatch, isGuest, setScreen]);

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

  const handleDeleteSelected = () => {
    dispatch(deletePosts(Array.from(selectedPosts)));
    setSelectedPosts(new Set());
    setDeleteMode(false);
  };

  if (loading) return <p>Loading posts...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
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
  );
}

export default Post;
