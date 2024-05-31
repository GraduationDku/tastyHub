import React, { useState, useEffect } from "react";
import Comment from "./Comment";

function PostDetails({ postId }) {
    const [postDetails, setPostDetails] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchPostDetails = async () => {
        try {
            const response = await fetch(`http://localhost:8080/post/detail/${postId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : localStorage.getItem('accessToken')
                }
            });
            if (response.ok) {
                const data = await response.json();
                setPostDetails(data);
            } else {
                throw new Error('Failed to fetch post details');
            }
        } catch (error) {
            console.error('Error fetching post details:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (postId) {
            fetchPostDetails();
        }
    }, [postId]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!postDetails) {
        return <div>No post details available</div>;
    }

    return (
        <div className="post-details">
            <h1>{postDetails.title}</h1>
            <p>{postDetails.text}</p>
            <p>{postDetails.postState}</p>
            <p>{postDetails.nickname}</p>
            <p>{postDetails.latestUpdateTime}</p>
            
            <Comment postId={postId} refreshComments={fetchPostDetails} />
            
            {postDetails.commentDtos.length > 0 ? (
                <ul>
                    {postDetails.commentDtos.map(comment => (
                        <li key={comment.userId}>
                            <p>{comment.nickname}</p>
                            <p><img src={comment.userImg} alt={`${comment.nickname}'s profile`} width="50" /></p>
                            <p>{comment.text}</p>
                            <p>{comment.state ? 'Active' : 'Inactive'}</p>
                            <p>{comment.latestUpdateTime}</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No comments</p>
            )}
        </div>
    );
}

export default PostDetails;
