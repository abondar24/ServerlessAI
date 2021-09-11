#!/bin/bash

node split-training-test.js ./data/reviews_Automotive_5.json ./data/automotive.json
node split-training-test.js ./data/reviews_Office_Products_5.json ./data/office.json
node split-training-test.js ./data/reviews_Beauty_5.json ./data/beauty.json
node split-training-test.js ./data/reviews_Pet_Supplies_5.json ./data/pet.json

node build-training-set.js ./data/automotive.json AUTO
node build-training-set.js ./data/office.json OFFICE
node build-training-set.js ./data/beauty.json BEAUTY
node build-training-set.js ./data/pet.json PET

cat ./data/AUTO ./data/OFFICE ./data/BEAUTY ./data/PET > ./data.csv

