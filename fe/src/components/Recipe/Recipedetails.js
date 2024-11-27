import React, { useState, useEffect, useRef } from 'react';
import ReactStars from 'react-rating-stars-component';
import LikeButton from '../Like/Like';
import ScrapButton from '../Scrap/Scrap';
import StarRatings from 'react-star-ratings';
import YouTube from 'react-youtube'; // YouTube 라이브러리
import '../../css/RecipeDetail.css';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);
  const [recipeReviews, setRecipeReviews] = useState([]);
  const [rating, setRating] = useState(0); // 별점 상태
  const [reviewContent, setReviewContent] = useState(''); // 리뷰 내용 상태
  const videoRef = useRef(null); // 비디오 컨트롤을 위한 ref
  const youtubePlayerRef = useRef(null); // YouTube 플레이어 ref

  useEffect(() => {
    async function fetchRecipeDetails() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/detail/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken'),
          },
        });
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setRecipeDetails(data);
        } else {
          throw new Error('Failed to fetch recipe details');
        }
      } catch (error) {
        console.error('Error fetching recipe details:', error);
      }
    }

    async function fetchRecipeReviews() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/list/${recipeId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('accessToken'),
          },
        });
        if (response.ok) {
          const reviewdata = await response.json();
          setRecipeReviews(reviewdata);
        } else {
          throw new Error('Failed to fetch recipe reviews');
        }
      } catch (error) {
        console.error('Error fetching recipe reviews:', error);
      }
    }

    if (recipeId) {
      fetchRecipeDetails();
      fetchRecipeReviews();
    }
  }, [recipeId]);

  const handleReviewSubmit = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe-review/create/${recipeId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken'),
        },
        body: JSON.stringify({ grade: rating, content: reviewContent }),
      });
  
      if (response.ok) {
        const newReview = { nickname: '사용자', grade: rating, content: reviewContent }; // 새 리뷰 데이터
        setRecipeReviews((prev) => [...prev, newReview]); // 리뷰 리스트 업데이트
        setRating(0); // 별점 초기화
        setReviewContent(''); // 리뷰 내용 초기화
      } else {
        console.error('Failed to submit review:', response.statusText);
      }
    } catch (error) {
      console.error('Error submitting review:', error);
    }
  };

  const convertToSeconds = (timeString) => {
    const timeParts = timeString.split(':').map(Number); // HH:MM:SS -> [HH, MM, SS]
    return timeParts.reduce((acc, val) => acc * 60 + val, 0); // 초 단위로 변환
  };

  const handleTimelineClick = (timeline) => {
    try {
      if (!timeline) return;
  
      const seconds = convertToSeconds(timeline); // HH:MM:SS를 초 단위로 변환
  
      if (recipeDetails.recipeType === 'Video' && videoRef.current) {
        // HTML5 비디오 플레이어
        videoRef.current.currentTime = seconds;
        videoRef.current.play();
      } else if (recipeDetails.recipeType === 'Youtube' && youtubePlayerRef.current) {
        // YouTube 플레이어
        youtubePlayerRef.current.seekTo(seconds, true); // 특정 시점으로 이동
      } else {
        console.warn('No valid video player available');
      }
    } catch (error) {
      console.error('Error handling timeline click:', error);
    }
  };
  

  if (!recipeDetails) {
    return <p>Loading...</p>;
  }

  // 유튜브 videoId 추출 로직
  const videoId = recipeDetails.foodVideoUrl?.includes('youtube.com')
    ? new URL(recipeDetails.foodVideoUrl).searchParams.get('v')
    : null;

  if (recipeDetails.recipeType === 'youtube' && !videoId) {
    console.error('Invalid YouTube URL:', recipeDetails.foodVideoUrl);
  }

  return (
    <div className="recipedetails">
      <br /><br />
      <div className="box">
        <h1
          style={{
            fontSize: '24px',
            marginBottom: '16px',
            textAlign: 'center',
            color: '#3eab5c',
          }}
        >
          {recipeDetails.foodName}
        </h1>
        {/* 대표 사진 */}
        <img
          src={recipeDetails.foodImgUrl}
          alt={recipeDetails.foodName}
          style={{
            width: '100%',
            height: 'auto',
            transition: 'transform 0.3s',
          }}
          onMouseOver={(e) => (e.currentTarget.style.transform = 'scale(1.05)')}
          onMouseOut={(e) => (e.currentTarget.style.transform = 'scale(1)')}
        />

        {/* 요리 설명 */}
        <div
          style={{
            backgroundColor: '#f9f9f9',
            borderRadius: '8px',
            padding: '30px',
            marginBottom: '16px',
            boxShadow: '0 2px 6px rgba(0, 0, 0, 0.1)',
          }}
        >
          <br />
          <p className="dis">
            <strong style={{ color: '#3eab5c' }}>레시피 설명<br /><br /></strong> {recipeDetails.foodInformation.content}
          </p>
          <p className="time">
            <strong style={{ color: '#3eab5c' }}>조리 시간 <br /><br /></strong> {recipeDetails.foodInformation.cookingTime}분
          </p>
          <p className="amount">
            {recipeDetails.foodInformation.serving}<strong style={{ color: '#3eab5c' }}>&nbsp;인분</strong>
          </p>
        </div>

        {/* 재료 */}
        <div>
          <h3>재료</h3>
          <ul className="recipedetail">
            {recipeDetails.ingredients.map((ingredient, index) => (
              <li className="recipedetail" key={ingredient.ingredientId || index}>
                {ingredient.ingredientName} - {ingredient.amount}
              </li>
            ))}
          </ul>
        </div>

        {/* 순서 */}
        {recipeDetails.recipeType === 'Video' && (
          <video
            ref={videoRef}
            src={require('../../../src/assets/sample2.mp4')}
            controls
            style={{ width: '100%', maxHeight: '300px' }}
          />
        )}

        {/* 유튜브 */}
        {recipeDetails.recipeType === 'Youtube' && recipeDetails.foodVideoUrl && videoId && (
          <YouTube
          videoId={videoId}
          opts={{
            width: '100%',
            height: '180px',
            playerVars: { controls: 1 },
          }}
          onReady={(e) => {
            youtubePlayerRef.current = e.target; // YouTube API 객체 저장
          }}
        />
        )}
        <div>
          <h3>순서</h3>
          <ol className="recipedetail">
            {recipeDetails.cookSteps.map((step, index) => (
              <li
                className="recipedetail"
                key={step.stepNumber || index}
                style={{
                  cursor: recipeDetails.recipeType !== 'Image' ? 'pointer' : 'default',
                }}
                onClick={() => handleTimelineClick(step.timeLine)}
              >
                {step.content}
                {recipeDetails.recipeType === 'Image' && step.stepImgUrl && (
                  <img
                    src={step.stepImgUrl}
                    alt={`Step ${step.stepNumber}`}
                    style={{ width: '100%', maxHeight: '200px', marginTop: '10px' }}
                  />
                )}
              </li>
            ))}
          </ol>
        </div>

        {/* 좋아요 & 스크랩 버튼 */}
        <p style={{ display: 'flex', gap: '20px' }}>
          <LikeButton className="recipedetail" recipeId={recipeId} />
          <ScrapButton recipeId={recipeId} />
        </p>
        <hr />
        {/* 리뷰 섹션 */}
        <div className="review" style={{ marginTop: '40px' }}>
          <h3 style={{ marginBottom: '20px', fontSize: '18px', fontWeight: 'bold', color: '#3eab5c' }}>
            리뷰
          </h3>
          <ul style={{ listStyleType: 'none', padding: '0', marginBottom: '20px' }}>
            {recipeReviews.map((review, index) => (
              <li
                key={index}
                style={{
                  marginBottom: '20px',
                  borderBottom: '1px solid #ddd',
                  paddingBottom: '10px',
                }}
              >
                <p style={{ marginBottom: '8px', fontWeight: 'bold' }}></p>
                <p style={{ marginBottom: '8px', color: '#ffd700' }}>
                  {Array.from({ length: 5 }, (_, i) => (
                    <span key={i} style={{ fontSize: '20px' }}>
                      {i < review.grade ? '★' : '☆'}
                    </span>
                  ))}
                </p>
                <p style={{ fontSize: '14px', color: '#555' }}>{review.content}</p>
              </li>
            ))}
          </ul>
          
          {/* 별점 및 리뷰 작성 */}
          <div
            style={{
              display: "flex", // 가로 배치를 위한 flex 컨테이너
              flexDirection: "row", // 가로 방향으로 배치
              justifyContent: "flex-start", // 왼쪽 정렬
              alignItems: "center", // 세로 중앙 정렬
              gap: "15px", // 별과 텍스트 영역 사이 간격
              width: "100%", // 컨테이너의 너비
            }}
          >
            <ReactStars
              count={5}
              value={rating} // 상태를 반영
              key={rating} // 강제로 재렌더링을 유도하기 위해 key 추가
              onChange={(newRating) => setRating(newRating)}
              size={20}
              activeColor="#ffd700"
            />
            {/* 텍스트 영역 */}
            <textarea
              placeholder="리뷰를 작성하세요"
              value={reviewContent}
              onChange={(e) => setReviewContent(e.target.value)}
              style={{
                width: "100%", // 텍스트 박스의 너비를 100%로 설정
                maxWidth: "400px", // 최대 너비 제한
                padding: "10px",
                border: "1px solid #ddd",
                borderRadius: "5px",
                resize: "none", // 사용자가 크기를 조정할 수 없도록 설정
                boxSizing: "border-box", // 패딩과 보더를 포함한 전체 크기를 맞춤
              }}
            ></textarea>
            <button
            onClick={handleReviewSubmit}
            style={{
              backgroundColor: '#3EAB5C',
              color: 'white',
              border: 'none',
              cursor: 'pointer',
              width:'80px',
              marginBottom :'20px'
            }}>
            제출
          </button>
          </div>


          
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
