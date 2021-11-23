// SPDX-License-Identifier: MIT
pragma solidity >=0.6.0 <0.8.0;

/**
 * @title burnable contract
 * @dev destruct the contract and send coins to itself
 */
contract BurnFish {

    constructor() payable {
        selfdestruct(payable(address(this)));
    }
}