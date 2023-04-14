import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair
const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJdR18r1pnLeG08w0HcizHpr83DLfqiivVmToaACP8hCfDejLBs7XdJ8MW8jn8tHg2fVkPT169HQDO97zQXtfcECAwEAAQ=='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

