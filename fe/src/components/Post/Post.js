import React, { useState, useEffect } from "react";
import '../../css/Post/Post.css';
import PageButton from '../../../src/components/PageButton.js'; // PageButton 컴포넌트 임포트

function Post({ setScreen, onPostSelect, isGuest }) {
  const [posts, setPosts] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(new Set());
  const [page, setPage] = useState(1); // 현재 페이지
  const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
  const [sort, setSort] = useState('date'); // 정렬 방식 기본값
  const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수

  useEffect(() => {
    if (isGuest) {
      setScreen('signup');
      return;
    }

    async function fetchAllPost() {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/post/list?page=${page}&size=${size}&sort=${sort}`, 
          {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('accessToken')
            }
          }
        );

        if (response.ok) {
          const data = await response.json();
          setPosts(data.content); // 받아온 게시글 설정
          setTotalItems(data.content.length); // 전체 게시글 수 계산 (posts 배열의 길이)
          setTotalPages(Math.ceil(data.content.length / size)); // 전체 페이지 수 계산
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
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/post/delete/${postId}`, 
          {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('accessToken')
            }
          }
        );
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
    setSize(parseInt(e.target.value, 10) || 5);
    setPage(1); // 페이지를 1로 초기화
  };

  const handleSortChange = (e) => {
    setSort(e.target.value || 'date');
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
              <option value="date">날짜</option>
              <option value="title">제목</option>
              <option value="nickname">작성자</option>
            </select>
        
            <label>게시글 수: </label>
            <select value={size} onChange={handleSizeChange}>
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
          {/* PageButton 컴포넌트 추가 */}
          <PageButton
            totalItems={totalItems} // 전체 게시글 수
            itemsPerPage={size} // 페이지당 게시글 수
            onPageChange={handlePageChange} // 페이지 변경 시 호출될 함수
          />
        </div>
      </div>
    </>
  );
}

export default Post;
