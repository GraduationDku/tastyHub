import React, {useState, useEffect} from "react";

function Post({setScreen, onPostSelect}){
  const [posts, setPosts] = useState([]);

  useEffect(()=> {
    async function fetchAllPost(){
      try{
        const response = await fetch('http://localhost:8080/post/list',{
          method: 'GET',
          headers:{
            'Content-Type':'application/json'
          }
        });
        if(response.ok){
          const authorization = response.headers.get('Authorization');
          const refreshToken = response.headers.get('Refresh');
          localStorage.setItem('accessToken', authorization);
          localStorage.setItem('refreshToken', refreshToken);
        }
        const data = await response.json();
        if(Array.isArray(data)){
          setPosts(data);
        }else{
          console.error('Invaild data format:',data);
        }
      }catch (error){
        console.error('Error fetching posts:', error);
      }
    }
    fetchAllPost();
  },[]);

  return(
    <><div>재료 공유 게시글 조회</div>
    <button onClick={() => setScreen('createpost')}>게시글 작성하기</button>
    <ul>
          {posts.map(post => (
            <li key={post.pagingPostResponseList.postId} onClick={() => onPostSelect(post.pagingPostResponseList.postId)}>
              <h3>{post.title}</h3>
              <div>
                <p> {post.pagingPostResponseList ? post.pagingPostResponseList.userImg : '정보 없음'} </p>
                <p> {post.pagingPostResponseList ? post.pagingPostResponseList.username : '정보 없음'}</p>
                <p> {post.pagingPostResponseList ? post.pagingPostResponseList.postState : '정보 없음'}</p>
              </div>
            </li>
          ))}
        </ul>

    </>
  )

}
export default Post;