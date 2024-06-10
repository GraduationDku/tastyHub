import React, { useState, useEffect } from 'react';

// 스크랩 목록을 가져오는 함수
const fetchScrapList = async (userId) => {
  try {
    const response = await fetch(`http://localhost:8080/scrap/list/${userId}`, {
      method : 'GET',
      headers: {
        'Authorization': localStorage.getItem('accessToken'),
        'Content-Type': 'application/json'
      }
    });
    const data = await response.json();
    return data.content;
  } catch (error) {
    console.error('Error fetching scrap list:', error);
    return [];
  }
};

const ScrapButton = ({ recipeId, onScrapChange }) => {
  const [scrapped, setScrapped] = useState(false);

  // 스크랩 상태를 변경하는 함수
  const toggleScrap = async () => {
    try {
      const response = await fetch(`/scrap/${recipeId}`, {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('accessToken'),
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        setScrapped(!scrapped);  // 로컬 상태 업데이트
        onScrapChange();  // 부모 컴포넌트에서 스크랩 목록 갱신
      } else {
        console.error('Error toggling scrap status:', response.statusText);
      }
    } catch (error) {
      console.error('Error toggling scrap status:', error);
    }
  };

  useEffect(() => {
    // 처음 로드될 때 스크랩 상태를 설정 (필요 시 서버에서 받아올 수 있음)
    setScrapped(true); // 예시로 기본 값을 true로 설정 (필요 시 변경)
  }, [recipeId]);

  return (
    <button onClick={toggleScrap}>
      {scrapped ? 'Unscrap' : 'Scrap'}
    </button>
  );
};

const ScrapList = ({ userId, token }) => {
  const [scrapList, setScrapList] = useState([]);

  const loadScrapList = async () => {
    const scraps = await fetchScrapList(userId, token);
    setScrapList(scraps);
  };

  useEffect(() => {
    loadScrapList();
  }, [userId, token]);

  return (
    <div>
      <h2>Scrap List</h2>
      <ul>
        {scrapList.map((item) => (
          <li key={item.foodId}>
            <img src={item.foodImgUrl} alt={item.foodName} />
            <span>{item.foodName}</span>
            <ScrapButton recipeId={item.foodId} token={token} onScrapChange={loadScrapList} />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ScrapList;
