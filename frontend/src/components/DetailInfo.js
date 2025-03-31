import Comment from "../pages/Comments";
import React, {useEffect, useState} from "react";
import axiosApi from "../util/AxiosRequests";
import {selectOptions} from "@testing-library/user-event/dist/select-options";
import {useNavigate} from "react-router-dom";

function DetailInfo() {
    const [detailInfo, setDetailInfo] = useState({});
    const [error, setError] = useState(false);
    const [postDate, setPostDate] = useState(new Date("2024-11-12T01:26:27.341113"))
    useEffect(() => {

        const getDetail = async () => {

            const id = getPassedItemID()
            try {
                const res = await axiosApi.get(`posts/get_post_details/id=${id}`)
                setDetailInfo(res.data["post"]);
                let pd = new Date(res.data["post"]["createdAt"])
                pd.setHours(pd.getHours() - 8);

                setPostDate(pd)
            } catch (error) {
                //setDetailInfo({"id":1,"title":"My good","author":"yiran","price":20.99,"tags":["electronic devices"],"content":"good stuff","createdAt":"2024-11-12T01:26:27.341113"})
                //setPostDate(new Date(detailInfo.createdAt))
                console.error(error)
                setError(true);
            }
        }
        getDetail();
    },[])

    return (
        <div>
            {error ?
                <p>Failed to load detail info</p>
                :
                <>
                    <GoodsInfo detailInfo={detailInfo} postDate={postDate}/>
                    <Comment/>
                </>}
        </div>
    );
}

function GoodsInfo({detailInfo, postDate}) {

    //const detailInfo = {title:"test",price:114.5,tag:["a","b"],description:"Amazing price!",image:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==",user:"testUser"};

    const [picture, setPicture] = useState(null)
    const [firstClick, setFirstClick] = useState(true)
    const [name, setName] = useState("");

    useEffect(() => {
        const ifAuthor = async () => {
            try {
                const id = await axiosApi.get("users/get_username/id=0");
                setName(id.data['username']);
            } catch (error) {
                console.log(error)
            }
        }
        const getPic = async () => {
            const id = getPassedItemID()
            try {
                const res = await axiosApi.get(`posts/get_post_image/id=${id}`, {responseType: "blob"})
                const imageUrl = URL.createObjectURL(res.data);
                setPicture(imageUrl);
            } catch (error) {
                console.log(error)
            }
        }
        getPic()
        ifAuthor()
    }, []);

    const handlRemove = async () => {
        if (firstClick) {
            setFirstClick(false)
        } else {
            try {
                const id = getPassedItemID()
                await axiosApi.post(`posts/delete/id=${id}`)
                alert("Remove successfully")
                window.location.reload();
            } catch (error) {
                console.log(error)
                alert("Remove failed");
            }
        }
    }

    return (
        <div style={{display: "flex", flexDirection: "column", gap: "20px"}}>
            {/* 顶部商品信息部分 */}
            <div style={{display: "flex", gap: "20px"}}>
                {/* info */}
                <div style={{flex: "1", display: "flex", flexDirection: "column", gap: "10px"}}>
                    {name === detailInfo.author ? <button
                        onClick={handlRemove}>{firstClick ? "Remove (item sold or no longer want to sell)" : "Are you sure you want to REMOVE the post?"}</button> : <> </>}
                    <div style={{display: "flex", justifyContent: "space-between"}}>
                        <span><strong>Name:</strong> {detailInfo.title}</span>
                        <span><strong>Price:</strong> ${detailInfo.price}</span>
                    </div>
                    <div style={{display: "flex", justifyContent: "space-between"}}>
                        <span><strong>Seller:</strong> {detailInfo.author}</span>
                        <span><strong>Post Date:</strong>{postDate.getFullYear()}/{1 + postDate.getMonth()}/{postDate.getDate()} {postDate.getHours()}:{String(postDate.getMinutes()).padStart(2, '0')}</span>
                    </div>
                    <span><strong>Tag:</strong> {detailInfo.tags ?
                        detailInfo.tags.join(", ") : ""}</span>
                    <div>
                        <p>{detailInfo.content}</p>
                    </div>
                    <div
                        style={{
                            fontWeight: "bold",
                            textAlign: "center",
                        }}
                    >
                        {detailInfo.status}
                    </div>
                </div>
                {/* picture */}
                <div style={{flex: "1", display: "flex", alignItems: "center", justifyContent: "center"}}>
                    {
                        picture ? (
                            <img
                                src={picture}
                                alt={detailInfo.title}
                                style={{
                                    maxWidth: "100%",
                                    height: "auto",
                                    borderRadius: "10px",
                                    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
                                }}
                            />

                        ) : (
                            <p>Loading image...</p>
                        )
                    }
                </div>
            </div>
        </div>
    );
}


function getPassedItemID() {
    return new URLSearchParams(window.location.search).get("itemID");
}

export default DetailInfo;