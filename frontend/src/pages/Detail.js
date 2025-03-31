import './css/Detail.css';
import TopNavigation from "../components/TopNavigation";
import {useNavigate} from "react-router-dom";
import {forwardRef, useEffect, useRef, useState} from "react";
import DetailInfo from "../components/DetailInfo";
import NewPost from "../components/NewPost";
import axiosApi from "../util/AxiosRequests";

const DEBUG = false;
// Page level
function DetailPage() {
    return (
        <div>
            <TopNavigation/>
            <Detail/>
        </div>
    );
}

// Detail level
function Detail() {
    const itemID = getPassedItemID();
    //const ID =getPassedItemID();
    const navigate = useNavigate();

    const [tag, setPostTags] = useState({});

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const response = await axiosApi.get('/post-tag/get-all');
                setPostTags(response.data['postTags']);
                //console.log('Post Tags:', response.data['postTags']);
            } catch (error) {
                if (DEBUG)
                    setPostTags({"test": 1, "test2": 2, "test3": 3});
                if (!DEBUG)
                    navigate("/v2/login?targetURL=" + window.location.href);
                //console.error('Error fetching post tags:', error);
            }
        };

        fetchTags();
    }, [navigate]);


    let component = <DefaultPage/>;
    if (new URLSearchParams(window.location.search).get("newpost") === "true") {
        component = <NewPost tag={tag}/>;
    }
    if (itemID !== null) {
        component = <DetailInfo/>;
    }


    return (
        <div className={"topContainer"}>
            <TagSelectBar tag={tag}/>

            <div className="container">
                <div className="left">
                    <PriceSelectBar/>
                    <ScrollableContainer tag={tag}/>
                </div>
                <div className={"right scrollbar scrollbar-vo"}>
                    {component}
                </div>
            </div>
        </div>
    );
}

function DefaultPage() {
    return (
        <h1>Click on the left part to view detail.</h1>
    );
}

// Tage select bar
function TagSelectBar({tag}) {
    return (
        <div className={"select-tag scrollbar scrollbar-ho scrollbar-hidden"}>
            <ul>
                {Object.keys(tag).map((key) => (
                    <Tag key={key} appear={key} tagID={tag[key]}/>
                ))}
            </ul>
        </div>
    );
}

// Different tag
function Tag({tagID, appear}) {
    let url = `/v2/detail?`;
    const currentTags = getPassedTagsString();
    let flag = false;
    for (let i = 0; i < currentTags.length; i++) {
        if (currentTags[i] === tagID.toString()) {
            flag = true;
            continue;
        }
        url += "&tag=" + currentTags[i].toString();
    }
    let IsSelect = "active";
    if (!flag) {
        url += "&tag=" + tagID
        IsSelect = "";
    }

    if (getPassedItemID() !== null)
        url += "&itemID=" + getPassedItemID();

    if (getMinPrice() !== null)
        url += "&minPrice=" + getMinPrice();
    if (getMaxPrice() !== null)
        url += "&maxPrice=" + getMaxPrice();

    url = url.replaceAll("?&", "?");

    return (
        <li><a className={IsSelect} href={url}>{appear}</a></li>
    );
}


function PriceSelectBar() {

    const OnClickE = () => {
        let inputMax = true;
        let inputMin = true;
        let minPrice = document.querySelector(".price-select input[placeholder='Min Price']").value;
        let maxPrice = document.querySelector(".price-select input[placeholder='Max Price']").value;

        inputMin = !(minPrice === "");
        minPrice = Number(minPrice);
        minPrice = minPrice > 0 ? minPrice : 0;

        inputMax = !(maxPrice === "");
        maxPrice = Number(maxPrice);
        maxPrice = maxPrice > 0 ? maxPrice : 0;

        if (minPrice > maxPrice && inputMin && inputMax) {
            [minPrice, maxPrice] = [maxPrice, minPrice];
        }

        let url = `/v2/detail?`;
        if (inputMin) {
            url += `minPrice=${minPrice}`;
        }
        if (inputMax) {
            url += `&maxPrice=${maxPrice}`;
        }
        if (getPassedTags().length > 0) {
            url += "&tag=" + getPassedTagsString().join("&tag=");
        }
        if (getPassedItemID() !== null) {
            url += "&itemID=" + getPassedItemID();
        }

        url = url.replaceAll("?&", "?");
        window.location.href = url;
    }

    const minPrice = getMinPrice();
    const maxPrice = getMaxPrice();

    return (
        <div className="price-select">
            <div className={"firstLine"}>
                <input type={"number"} min={0} placeholder={"Min Price"} defaultValue={minPrice}></input> - <input
                type={"number"} min={0}
                placeholder={"Max Price"} defaultValue={maxPrice}></input>
                <button onClick={OnClickE}>Apply</button>
            </div>
        </div>
    )
}


function ScrollableContainer() {
    const targetRef = useRef(null);

    const [items, setItems] = useState([]);
    const [haveItem, setHaveItem] = useState(true);

    useEffect(() => {
        const getRetrieve = async () => {
            try {
                if (getIfWantBuy() !== null) {
                    const id = getIfWantBuy();
                    const res = (await axiosApi.get(`posts/get_user_favorite_posts/id=${id}`)).data;
                    if (res === null) {
                        console.log("Some Error Happened");
                    } else if (res.count === 0) {
                        setHaveItem(false);
                    } else {
                        setHaveItem(true);
                        setItems(res.posts);
                    }
                } else if (getIfMyItem() !== null) {
                    const id = getIfMyItem();
                    const res = (await axiosApi.get(`posts/get_post_of_user/id=${id}`)).data;
                    if (res === null) {
                        console.log("Some Error Happened");
                    } else if (res.count === 0) {
                        setHaveItem(false);
                    } else {
                        setHaveItem(true);
                        setItems(res.posts);
                    }
                } else {
                    let postMes;
                    postMes = {};
                    if (getMaxPrice() != null) {
                        postMes.maxPrice = parseFloat(getMaxPrice());
                    }
                    if (getMinPrice() != null) {
                        postMes.minPrice = parseFloat(getMinPrice());
                    }
                    if (getPassedTagsString() != null && getPassedTagsString().length !== 0) {
                        const tag = getPassedTags();
                        postMes.tags = tag;
                    }

                    const res = (await axiosApi.post("posts/retrieve", postMes)).data;
                    if (res === null) {
                        console.log("Some Error Happened");
                    } else if (res.count === 0) {
                        setHaveItem(false);
                    } else {
                        setHaveItem(true);
                        setItems(res.posts);
                    }

                }
            } catch (error) {
                console.log(error);
                if (DEBUG) {
                    setHaveItem(true)
                    setItems([
                        {
                            "id": 1,
                            "title": "My good",
                            "author": "yiran",
                            "price": 20.99,
                            "tags": [
                                "electronic devices"
                            ],
                            "content": "good stuff",
                            "createdAt": "2024-11-12T01:26:27.341113"
                        },
                    ]);
                }
            }
        }

        getRetrieve();

    }, []);


    useEffect(() => {
        if (targetRef.current) {
            targetRef.current.scrollIntoView({behavior: 'instant', block: 'center'});
        }
    }, [items]);

    return (
        <div className="scrollable-container scrollbar scrollbar-vo">
            {(haveItem ? items.map((item, index) => (
                    <SelectBar
                        key={item.id}
                        ref={item.id.toString() === getPassedItemID() ? targetRef : null}
                        {...item}
                    />
                ))
                : "No item")}
        </div>
    );
}


const SelectBar = forwardRef(({id, title, price, tags}, ref) => {
    const tag = tags || [];
    let className = "";

    if (getPassedItemID() === id.toString()) {
        className = "Select";
    }

    let url = `/v2/detail?itemID=${id}`;
    if (getPassedTags().length > 0) {
        url += "&tag=" + getPassedTags().join("&tag=");
    }

    if (getMinPrice() !== null)
        url += "&minPrice=" + getMinPrice();
    if (getMaxPrice() !== null)
        url += "&maxPrice=" + getMaxPrice();

    if (getIfWantBuy() !== null)
        url += "&wantbuy=" + getIfWantBuy();
    if (getIfMyItem() !== null)
        url += "&myitem=" + getIfMyItem();


    return (
        <div ref={ref} className={`select SelectBar ${className}`}>
            <a href={url}>
                <div className="upper">
                    <h4>{title}</h4>
                    <h2>${price}</h2>
                </div>
                <p>{Array.isArray(tag) ? tag.join(" ") : tag}</p>
            </a>
        </div>
    );
});


function getPassedItemID() {
    return new URLSearchParams(window.location.search).get("itemID");
}

function getPassedTags() {
    return new URLSearchParams(window.location.search).getAll("tag").map((value => parseInt(value)));
}

function getPassedTagsString() {
    return new URLSearchParams(window.location.search).getAll("tag");
}

function getMinPrice() {
    return new URLSearchParams(window.location.search).get("minPrice");
}

function getMaxPrice() {
    return new URLSearchParams(window.location.search).get("maxPrice");
}

function getIfMyItem() {
    return new URLSearchParams(window.location.search).get("myitem");
}

function getIfWantBuy() {
    return new URLSearchParams(window.location.search).get("wantbuy");
}

export default DetailPage;