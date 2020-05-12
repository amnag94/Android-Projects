var express = require('express');
var app = express();
var users = require('./user');

//import Ingredients from './users';

var ingredients = new users.Ingredients();

app.get('/ingredients', (req, res) => {
    res.type('json');
    res.send(ingredients.get_ingredients());
});

app.get('/ingredients/:ingred', (req, res) => {
    res.type('json');
    res.send(ingredients.get_ingredient(req.params.ingred));
});

app.put('/ingredients/add/:ingred/:measure/:quantity', (req, res) => {
    result = ingredients.add_ingredient(req.params.ingred, req.params.measure, req.params.quantity);
    res.send(`${req.params.ingred} ${result}`);
});

app.put('/ingredients/empty', (req, res) => {
    res.send(ingredients.empty_ingredients());
});

app.listen(3000);