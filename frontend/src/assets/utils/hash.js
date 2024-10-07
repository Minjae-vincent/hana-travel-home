import CryptoJS from 'crypto-js'

export function hashAndLowercase(input) {
  const hash = CryptoJS.SHA256(input).toString(CryptoJS.enc.Hex)
  return hash.toLowerCase()
}
