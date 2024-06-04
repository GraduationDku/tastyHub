import React, { useState } from "react";

const Comment = ({ postId, refreshComments }) => {
    const [text, setText] = useState('');
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editingText, setEditingText] = useState('');

    const createComment = async () => {
        try {
            const response = await fetch(`http://localhost:8080/comment/create/${postId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : localStorage.getItem('accessToken')
                },
                body: JSON.stringify({ text })
            });
            if (response.ok) {
                setText('');
                refreshComments();
            } else {
                throw new Error('Failed to create comment');
            }
        } catch (error) {
            console.error('Error creating comment:', error);
        }
    };

    const editComment = async (commentId) => {
        try {
            const response = await fetch(`http://localhost:8080/comment/modify/${commentId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ text: editingText })
            });
            if (response.ok) {
                const authorization = response.headers.get('Authorization');
                const refreshToken = response.headers.get('Refresh');
                localStorage.setItem('accessToken', authorization);
                localStorage.setItem('refreshToken', refreshToken);
                setEditingCommentId(null);
                setEditingText('');
                refreshComments();
            } else {
                throw new Error('Failed to edit comment');
            }
        } catch (error) {
            console.error('Error editing comment:', error);
        }
    };

    const deleteComment = async (commentId) => {
        try {
            const response = await fetch(`http://localhost:8080/comment/delete/${commentId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                const authorization = response.headers.get('Authorization');
                const refreshToken = response.headers.get('Refresh');
                localStorage.setItem('accessToken', authorization);
                localStorage.setItem('refreshToken', refreshToken);
                refreshComments();
            } else {
                throw new Error('Failed to delete comment');
            }
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    return (
        <div className="comments-section">
            <h2>Leave a Comment</h2>
            <textarea
                value={text}
                onChange={(e) => setText(e.target.value)}
                placeholder="Write a comment..."
            ></textarea>
            <button onClick={createComment}>Submit</button>

            {postDetails.commentDtos.map(comment => (
                <div key={comment.userId}>
                    {editingCommentId === comment.userId ? (
                        <>
                            <textarea
                                value={editingText}
                                onChange={(e) => setEditingText(e.target.value)}
                            ></textarea>
                            <button onClick={() => editComment(comment.userId)}>Save</button>
                            <button onClick={() => setEditingCommentId(null)}>Cancel</button>
                        </>
                    ) : (
                        <>
                            <p>{comment.text}</p>
                            <button onClick={() => setEditingCommentId(comment.userId)}>Edit</button>
                            <button onClick={() => deleteComment(comment.userId)}>Delete</button>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
};

export default Comment;
