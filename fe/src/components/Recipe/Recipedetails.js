import React, { useState, useEffect, useRef } from 'react';
import CreateRecipeReview from '../Review/CreateRecipeReview';
import LikeButton from '../Like/Like';
import ScrapButton from '../Scrap/Scrap';
import YouTube from 'react-youtube'; // YouTube 라이브러리
import '../../css/RecipeDetail.css';

function RecipeDetails({ recipeId }) {
  const [recipeDetails, setRecipeDetails] = useState(null);
  const [recipeReviews, setRecipeReviews] = useState([]);
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

  // HH:MM:SS를 초 단위로 변환하는 함수
  const convertToSeconds = (timeString) => {
    const timeParts = timeString.split(':').map(Number); // HH:MM:SS -> [HH, MM, SS]
    return timeParts.reduce((acc, val) => acc * 60 + val, 0); // 초 단위로 변환
  };

  const handleTimelineClick = (timeline) => {
    try {
      if (!timeline) return;

      const seconds = convertToSeconds(timeline); // HH:MM:SS를 초 단위로 변환

      if (videoRef.current) {
        videoRef.current.currentTime = seconds; // HTML5 비디오 플레이어의 시간 설정
        videoRef.current.play(); // 비디오 재생
      } else {
        console.warn('Video player is not available');
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
      <h1>{recipeDetails.foodName}</h1>
      <div className="box">
        {/* 대표 사진 */}
        <img src={recipeDetails.foodImgUrl} alt={recipeDetails.foodName} />

        {/* 요리 설명 */}
        <div>
          <p className="dis">
            <strong>레시피 설명<br /><br /></strong> {recipeDetails.foodInformation.content}
          </p>
          <p className="time">
            <strong>조리 시간 </strong> {recipeDetails.foodInformation.cookingTime}분
          </p>
          <p className="amount">
            <strong>양 </strong> {recipeDetails.foodInformation.serving}
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
        {/* 비디오 */}
        {/* {recipeDetails.recipeType === 'Video' && recipeDetails.foodVideoUrl && (
          <video
            ref={videoRef}
            src={'https://tastyhub-bucket.s3.ap-northeast-2.amazonaws.com/videos/sample2.mp4'}
            controls
            style={{ width: '100%', maxHeight: '500px' }}
          />
)} */}
        {recipeDetails.recipeType === 'Video' && (
          <video
            ref={videoRef}
            src={require('/Users/sep037/Desktop/fe/fe/src/assets/sample2.mp4')}
            controls
            style={{ width: '100%', maxHeight: '300px' }}
          />
        )}

        {/* 유튜브 */}
        {recipeDetails.recipeType === 'Youtube' && recipeDetails.foodVideoUrl && videoId && (
          <YouTube
            videoId={videoId}
            opts={{ width: '100%', height: '500px', playerVars: { controls: 1 } }}
            onReady={(e) => (youtubePlayerRef.current = e.target)}
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
                  cursor: recipeDetails.recipeType !== 'photo' ? 'pointer' : 'default',
                }}
                onClick={() => handleTimelineClick(step.timeLine)}
              >
                {step.content}

                {/* 단계별 이미지 */}
                {recipeDetails.recipeType === 'photo' && step.cookStepImg && (
                  <img
                    src={step.cookStepImg}
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

        {/* 리뷰 */}
        <div className="review">
          <div>
            <ul>
              {recipeReviews.map((review, index) => (
                <li key={index}>
                  <p>
                    <strong>{review.nickname}</strong> (평점 : {review.grade})
                  </p>
                  <p>{review.content}</p>
                </li>
              ))}
            </ul>
          </div>
          <div>
            <CreateRecipeReview recipeId={recipeId} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default RecipeDetails;
