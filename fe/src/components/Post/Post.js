import React, { useState, useEffect } from "react";
import '../../css/Post/Post.css';

function Post({ setScreen, onPostSelect, isGuest }) {
  const [posts, setPosts] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(new Set());
  const [page, setPage] = useState(1); // 숫자로 초기화
  const [size, setSize] = useState('');
  const [sort, setSort] = useState('');
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    if (isGuest) {
      setScreen('signup');
      return;
    }

    async function fetchAllPost() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/post/list?page=${page}&size=${size}&sort=${sort}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken')
          }
        });
        if (response.ok) {
          const data = await response.json();
          setPosts(data.content);
          setTotalPages(data.totalPages); // 전체 페이지 수 설정
        }
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    }

    fetchAllPost();
  }, [page, size, sort, isGuest, setScreen]);

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
        const response = await fetch(`${process.env.REACT_APP_API_URL}/post/delete/${postId}`, {
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

  const handleSizeChange = (e) => {
    setSize(e.target.value || '');
    setPage(1); // 페이지를 1로 초기화
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || '');
    setPage(1); // 페이지를 1로 초기화
  };

  const handlePageChange = (newPage) => {
    if (newPage < 1) newPage = 1; // 페이지 번호를 1보다 작지 않도록 설정
    if (newPage > totalPages) newPage = totalPages; // 페이지 번호를 전체 페이지 수보다 크지 않도록 설정
    setPage(newPage);
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
          <div>
            <label>정렬 기준: </label>
            <select value={sort} onChange={handleSortChange}>
              <option value="">기본 정렬</option>
              <option value="date">날짜</option>
              <option value="title">제목</option>
              <option value="nickname">작성자</option>
            </select>
          </div>
          <div>
            <label>게시글 수: </label>
            <select value={size} onChange={handleSizeChange}>
              <option value="">기본</option>
              <option value={5}>5개</option>
              <option value={10}>10개</option>
              <option value={20}>20개</option>
            </select>
          </div>
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
          <div className="pagination">
            <button onClick={() => handlePageChange(page - 1)} disabled={page <= 1}>
              이전 페이지
            </button>
            <span>{page} / {totalPages}</span>
            <button onClick={() => handlePageChange(page + 1)} disabled={page >= totalPages}>
              다음 페이지
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default Post;
