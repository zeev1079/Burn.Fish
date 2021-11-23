// SPDX-License-Identifier: MIT
pragma solidity >=0.6.0 <0.8.0;

import "./BurnFish.sol";

/**
 * @title coin burning
 * @dev Implements the a contract to create BurnFish contract
 */
contract FishBurner {

    function purge() public payable returns (address){
        BurnFish burn_address = (new BurnFish){value : msg.value}();
        return address(burn_address);
    }
}