import "./css/Comment.css"
import React, {useEffect, useState} from "react"
import axiosApi from "../util/AxiosRequests";
import {post} from "axios";

function Comment() {

    const [allCommentInfo, SetCommentInfo] = useState([]);
    const [displayInfo, setDisplayInfo] = useState("Reply:");
    const [isFavorite, setFavorite] = useState(false);
    const [isPublic, setPublic] = useState(true);

    useEffect(() => {
        const getComments = async () => {
            try {
                const postID = getPassedItemID();
                const response = await axiosApi.get(`comments/get_comments_of_post_id/id=${postID}`);
                const data = response.data;

                console.log(response);
                if (data && data.comments) {
                    SetCommentInfo(data.comments);
                } else {
                    console.error('Invalid response structure:', response.data);
                }

                const res = await axiosApi.get(`posts/get_post_favorite_user_ids/id=${postID}`)
                const allFavoriteUser = res.data;
                if (allFavoriteUser['count'] !== 0) {
                    const id = await axiosApi.get("/users/get_logged_in_user_id");
                    if (allFavoriteUser['userIds'].includes(id.data['id'])) {
                        setFavorite(true);
                    }
                }
            } catch (error) {
                console.error(error);
            }
        };

        getComments();
    }, []);

    const [selected, setSelect] = useState(0);

    const ReplyRoot = () => {
        setSelect(0);
        setDisplayInfo("Reply:");
    }
    const FavoritePost = async () => {
        try {
            if (isFavorite)
                await axiosApi.post("/posts/cancel_favorite", {"postId": parseInt(getPassedItemID())})
            else
                await axiosApi.post("/posts/favorite", {"postId": parseInt(getPassedItemID())});
            setFavorite(!isFavorite);
        } catch (error) {
            console.error("Can not favorite");
        }
    }

    return (
        <div>

            <button onClick={ReplyRoot}>Add Comment</button>
            <button onClick={() => setPublic(!isPublic)}>Set to {isPublic ? " private" : "public"}</button>

            <button onClick={FavoritePost}>{isFavorite ? "Unlike" : "Like"}</button>
            {}


            <hr/>
            <div>
                {
                    allCommentInfo.map((comment) => (
                        <ViewComments setSelect={setSelect} selected={selected} comments={comment}
                                      setDisplayInfo={setDisplayInfo}/>
                    ))
                }

            </div>

            <NewComment id={1} selected={selected} displayInfo={displayInfo} isPublic={isPublic}/>

        </div>
    );
}

function NewComment({selected, displayInfo,isPublic}) {
    const SubmitNewComment = async (event) => {
        event.preventDefault();
        const comment = event.target.contentDetail.value;

        try {
            if (selected === 0) {
                await axiosApi.post("comments/add_to_post", {postId: getPassedItemID(), content: comment,isPublic:isPublic})
            } else {
                await axiosApi.post("comments/add_to_comment", {commentId: selected, content: comment,isPublic:isPublic})
            }

            window.location.reload();
        } catch (error) {
            alert("Failed Post Comment");
            console.error("Not Post");
        }
    }
    return (
        <div className={"newCommentArea"}>
            <hr style={{backgroundColor: "transparent", borderColor: "transparent"}}></hr>
            <h4 style={{marginTop: "10px"}}> Talk to the goods holder: {isPublic? "":"(Private)"}</h4>
            <hr style={{backgroundColor: "rgba(36,83,126,0.46)"}}></hr>
            <form onSubmit={SubmitNewComment}>
                <textarea name={"contentDetail"} placeholder={displayInfo} className={"fullwidth"} required></textarea>
                <button type={"submit"}>Submit</button>
            </form>
        </div>
    );
}

function ViewComments({setSelect, selected, comments, setDisplayInfo}) {
    const currentCommentInfo = comments["information"];
    const subComments = comments["subComments"];
    const tryOnClick = () => {
        setSelect(currentCommentInfo["id"]);
        const currentComment = currentCommentInfo["content"];
        setDisplayInfo("Replying to " + currentCommentInfo["authorName"] + ": " + ((currentComment.length > 60) ? (currentComment.substring(0, 60) + "...") : currentComment))
    }

    const postDate = new Date( currentCommentInfo["createdAt"])
    postDate.setHours(postDate.getHours() - 8);
    return (
        <div>
            <p>{currentCommentInfo["authorName"]} {currentCommentInfo["isPubic"] ? "" : "(private post)"}: {currentCommentInfo["content"]}</p>
            <button onClick={tryOnClick}>reply</button>
            <span style={{
                "margin-left": " 40px",
                fontSize: "small",
                color: "rgba(119,119,119,0.82)"
            }}>{postDate.getFullYear()}/{1 + postDate.getMonth()}/{postDate.getDate()} {postDate.getHours()}:{postDate.getMinutes()}</span>


            <div style={{"margin-left": " 40px"}}>
                {
                    subComments.map((comment) => (
                        <ViewComments setSelect={setSelect} selected={selected} comments={comment}
                                      setDisplayInfo={setDisplayInfo}/>
                    ))
                }
            </div>
        </div>

    );
}

function getPassedItemID() {
    return new URLSearchParams(window.location.search).get("itemID");
}

export default Comment;