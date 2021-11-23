package scalajs.web3.ethersjs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal


@js.native
@JSGlobal
class Contract extends js.Object {
	val address: String = js.native
}

@js.native
trait BigNumber extends js.Object {
	def toNumber(): Double

	def toString(): String

	def toHexString(): String
}

@js.native
trait Web3Provider extends js.Object {
	def getSigner(): js.Dynamic = js.native
}

@js.native
trait Providers extends js.Object {
	def Web3Provider: js.Dynamic = js.native
}

@js.native
trait Utils extends js.Object {
	def parseEther(etherString: String): BigNumber = js.native
}

@js.native
trait ContractFactory extends js.Object {
	def deploy(args: js.Dynamic): js.Promise[Contract] = js.native
}

@js.native
@JSGlobal("ethers")
class Ethers extends js.Object {
	def providers: Providers = js.native

	def utils: Utils = js.native

	def ContractFactory: js.Dynamic = js.native
}

@js.native
trait Ethereum extends js.Object {
	def request(args: js.Dynamic): js.Promise[js.Any]
}


