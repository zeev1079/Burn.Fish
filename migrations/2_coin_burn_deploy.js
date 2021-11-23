const FishBurner = artifacts.require("FishBurner");
const BurnFish = artifacts.require("BurnFish");

module.exports = function(deployer) {
    deployer.deploy(FishBurner);
    deployer.deploy(BurnFish);
};