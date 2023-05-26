import * as React from 'react';
import LoginForm from "../items/LoginForm";
import {getUser, request, setAuthToken, setUserId, setUserLogin} from "../auth/axios_helper";
import * as ComponentNames from "../constants/ComponentNames";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import * as AppEndPoints from "../constants/AppEndPoints";
import {Navigate, useLocation, useMatch, useNavigate, useResolvedPath} from "react-router-dom";
import {ErrorModalWindow} from "../items/ErrorModalWindow";
import {useContext, useEffect, useState} from "react";
import {UserContext} from "../context/Context";
import * as ErrorMessageConstants from "../constants/ErrorMessageConstants";
import ReactDOM from 'react-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import "../css/App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import ReactPaginate from "https://cdn.skypack.dev/react-paginate@7.1.3";


export default function FavoriteWords() {

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [items, setItems] = useState([]);
    const [active, setActive] = useState(0);


    useEffect(() => {
        request("POST", ApiEndPoints.DICTIONARY_FAVORITES,
            {
                page: page,
                size: size
            }).then((response) => {
            console.log(response);
            setItems(response.data.itemPage);
            setTotal(response.data.totalPages);
            setSize(response.data.totalElements)
            setActive(response.data.currentPage)
        }).catch((error) => {
            console.error(error);
        });
    }, []);


    return (
        <div className="m-4">
            <ItemWords items={items}/>
            <nav aria-label="...">
                <ul className="pagination pagination-lg">
                    <Items items={total}/>
                </ul>
            </nav>
        </div>
    )
}

function Items(props) {
    const amount = props.amount;
    const list = [];
    for (let i = 0; i < amount; i++) {
        list.push(<li className="page-item"><a className="page-link">{i}</a></li>)
    }

    return list;
}


function ItemWords(props) {
    const items = props.items;
    const listItems = items.map((key, index) =>
        <div className="card">
            <div className="card-header">
                {key.type}
            </div>
            <div className="card-body">
                <h5 className="card-title">{key.originalWord}</h5>
                <p className="card-text">Synonyms amount: {key.synonyms.length} </p>
                <b/>
                <p className="card-text">Meanings amount: {key.meaning.length} </p>
                <div><i className="glyphicon glyphicon-star-empty"></i>
                    <i className="glyphicon glyphicon-star"></i></div>
            </div>


        </div>
    );
    return listItems;
}


