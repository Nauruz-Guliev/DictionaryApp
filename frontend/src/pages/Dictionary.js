import * as React from "react";
import {Navigate, useLocation, useNavigate} from "react-router-dom";
import {getAuthToken, request} from "../auth/axios_helper";
import * as ApiEndPoints from "../constants/ApiEndPoints";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import Carousel from 'nuka-carousel';
import * as AppEndPoints from "../constants/AppEndPoints";
import "../css/App.css";
import 'bootstrap-icons/font/bootstrap-icons.css';
import {ErrorModalWindow} from "../items/ErrorModalWindow";
import * as ErrorMessageConstants from "../constants/ErrorMessageConstants";


export function Dictionary() {
    const location = useLocation();
    try {
        return (<DictionaryScreen model={location.state.model.word}/>);
    } catch (e) {
        return <Navigate to={AppEndPoints.TRANSLATION} replace={true}/>
    }
}

class DictionaryScreen extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            model: props.model,
            error: {},
            navigateHome: false,
            showModal: false
        }
    }

    componentDidMount() {
        try {
            request("POST", ApiEndPoints.DICTIONARY, {
                text: this.state.model.text, from: this.state.model.fromLocale, to: this.state.model.toLocale,
            })
                .then((response) => {
                    this.setState({data: response.data});
                    console.log(this.state.data.dictionary)
                }).catch((error) => {
                    console.error(error);
                    try {
                        this.setState({
                            navigateToError: true,
                            error: error.response.data.message,
                            showModal: true,
                        });
                    } catch (e) {
                        this.setState({
                            navigateToError: true,
                            error: ErrorMessageConstants.ERROR_FETCHING,
                            showModal: true,
                        });
                    }

            })
        } catch (e) {
            this.setState({
                navigateToError: true,
                error: ErrorMessageConstants.ERROR_FETCHING,
                showModal: true,
            })
        }
    }


    render() {
        const items = this.state.data.dictionary;
        if (!items) return;
        const listItems = items.map((word) => <div className="card w-100 mt-2">
                <div className="row">
                    <SubItems items={word.synonyms} title={"Synonyms"}/>
                    <SubItems items={word.meaning} title={"Meaning"}/>
                    <div className="w-50 h-25  justify-content-center">
                        <ImageCarousel items={this.state.data.imageUrls}/>
                        <button type="button" id="favoriteButton"
                                className={word.favorite ? "btn btn-warning align-self-start p-1" : "btn btn-secondary align-self-start p-1"}
                                onClick={() => {
                                    ToggleFavorite(word).then(r => {
                                            this.updateList(word);
                                        }
                                    );
                                }}>
                            {word.favorite ? <i className="bi bi-star-fill">Remove from favorites</i> :
                                <i className="bi bi-star"> Add to favorites</i>}
                        </button>
                    </div>
                    <ErrorModalWindow
                        show={this.state.showModal}
                        onHide={() => this.setState({showModal: false, navigateToError: true})}
                        message={this.state.error}/>
                </div>
            </div>
        );

        //                <Items items={this.state.data.dictionary} images={this.state.data.imageUrls}/>
        return (
            <div>
                {listItems}
                {this.state.navigateToError && getAuthToken() && <Navigate to={AppEndPoints.HOME} replace={true}/>}
            </div>
        )
    }

    updateList(word) {
        const newState = this.state.data;
        const index = newState.dictionary.findIndex(a => a.id === word.id);
        if (index === -1) return;
        newState.dictionary.splice(index, 1);
        word.favorite = !word.favorite;
        newState.dictionary.push(word);
        this.setState(newState);
    }
}


async function ToggleFavorite(props) {
    if (props.favorite) {
        return await RemoveFromFavorite(props);
    } else {
        return await AddFavorite(props);
    }
}

async function RemoveFromFavorite(props) {
    let word = props;
    let result;
    await request("POST", ApiEndPoints.REMOVE_FAVORITE, word)
        .then((response) => {
            result = response.data;
        }).catch((error) => {
            console.log(error.response.data)
        })
    return result;
}

async function AddFavorite(props) {
    let word = props;
    let result;
    await request("POST", ApiEndPoints.ADD_FAVORITE, word)
        .then((response) => {
            result = response.data;
        }).catch((error) => {
            console.log(error.response.data)
        })
    return result;
}


function SubItems(props) {
    const items = props.items;
    if (!items) return;
    const listItems = items.map((word) => <li className="list-group-item"> {word}</li>);
    return (<div className="card w-25">
        <h1 className="display-8">{props.title}</h1>
        <ul className=" list-group list-group-flush">
            {listItems}
        </ul>
    </div>)
}


function ImageCarousel(props) {
    const images = props.items;
    if (!images) return;
    const listItems = images.map((word) => <div>
        <img src={word} className=" rounded float-start" id="carouselImage" alt="image"/>
    </div>);

    return (
        <div id="carousel">
            <Carousel>
                {listItems}
            </Carousel>
        </div>
    )
}

