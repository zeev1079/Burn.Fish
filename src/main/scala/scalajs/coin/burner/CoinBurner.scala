package scalajs.coin.burner

import org.scalajs.dom
import org.scalajs.dom.document
import scalajs.vuejs.vue.Vue
import scalajs.web3.ethersjs._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scala.scalajs.js.JSON

object CoinBurner {
	private final val CONTRACT_ABI = JSON.parse(
		"""[
		  |                    {
		  |                        "inputs": [],
		  |                        "stateMutability": "payable",
		  |                        "type": "constructor",
		  |                        "payable": true
		  |                    }
		  |                ]""".stripMargin)
	private final val CONTRACT_BYTE_CODE = "0x60806040523073ffffffffffffffffffffffffffffffffffffffff16fffe"

	@js.native
	trait Data extends Vue {
		var connected: Boolean = js.native
		var burnPending: Boolean = js.native
		var signer: js.Dynamic = js.native
		var contractBurnAddress: String = js.native
		var ethBurnValue: String = js.native
	}

	type VueMethod = js.ThisFunction0[Data, Unit]


	private def vueConnect(data: Data) = {
		val ehterum = js.Dynamic.global.ethereum.asInstanceOf[Ethereum]
		ehterum.request(js.Dynamic.literal(method = "eth_requestAccounts")).toFuture.onComplete { _ =>
			val provider = js.Dynamic.newInstance(js.Dynamic.global.ethers.asInstanceOf[Ethers].providers.Web3Provider)(js.Dynamic.global.ethereum.asInstanceOf[Ethereum])
			data.signer = provider.getSigner()
			data.connected = true
		}
	}

	def executeBurnContract(data: Data): Unit = {
		if (data.connected) {
			data.burnPending = true
			val weiCoins = js.Dynamic.global.ethers.asInstanceOf[Ethers].utils.parseEther(data.ethBurnValue)
			val burnerFactory = js.Dynamic.newInstance(js.Dynamic.global.ethers.asInstanceOf[Ethers].ContractFactory)(CONTRACT_ABI, CONTRACT_BYTE_CODE, data.signer).asInstanceOf[ContractFactory]
			val deployPromise = burnerFactory.deploy(js.Dynamic.literal(value = weiCoins))
			for (contract <- deployPromise.toFuture) {
				data.contractBurnAddress = contract.address
				data.burnPending = false
			}
		} else dom.window.alert("To use this app you'll need to connect to metamask'")
	}


	val connect = { (data: Data) =>
		if (js.typeOf(js.Dynamic.global.ethereum) != "undefined") vueConnect(data)
		else dom.window.alert("To use this app you'll need metamask or another web3 provider.'")
	}: VueMethod

	val burnCoins = ((data: Data) => executeBurnContract(data)): VueMethod


	def setupUI(): Unit = {
		val app = new Vue(
			literal(
				el = "#vue-burner",
				data = literal(
					connected = false,
					burnPending = false,
					signer = null,
					contractBurnAddress = null,
					ethBurnValue = null
				),
				methods = literal(
					connect = connect,
					burnCoins = burnCoins
				)
			)
		)
	}

	def main(args: Array[String]): Unit = {
		document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
			setupUI()
		})
	}

}
