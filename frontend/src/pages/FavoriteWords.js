import * as React from 'react';
import {useEffect, useState} from 'react';
import {request} from "../auth/axios_helper";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import 'bootstrap-icons/font/bootstrap-icons.css';
import "../css/App.css";
import "bootstrap/dist/css/bootstrap.min.css";


export default function FavoriteWords() {

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);
    const [total, setTotal] = useState(0);
    const [items, setItems] = useState([]);
    const [active, setActive] = useState(0);


    const loadData = (pageNumber) => {
        console.log(pageNumber);
        request("POST", ApiEndPoints.DICTIONARY_FAVORITES,
            {
                page: pageNumber,
                size: size
            }).then((response) => {
            console.log(response);
            setItems(response.data.itemPage);
            setTotal(response.data.totalPages);
            setActive(response.data.currentPage)
        }).catch((error) => {
            console.error(error);
        });
    }
    useEffect(() => {
        loadData(page)
    }, []);


    function Items() {
        const list = [];
        for (let i = 0; i < total; i++) {
            if (i !== active) {
                list.push(<li className="page-item">
                    <button className="page-link" onClick={() => {
                        loadData(i);
                    }
                    }>{i+1}</button>
                </li>)
            } else {
                list.push(<li className="page-item active">
                    <button className="page-link">{i+1}</button>
                </li>)
            }
        }
        return list;
    }


    return (
        <div className="m-4">
            <ItemWords items={items}/>
            <nav aria-label="...">
                <ul className="pagination pagination-lg">
                    <Items/>
                </ul>
            </nav>
        </div>
    )
}


function ItemWords(props) {
    const items = props.items;
    return items.map((key, index) =>
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
}


