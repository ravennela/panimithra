# 🚀 Flutter App - Razorpay Integration Guide

## ⚠️ IMPORTANT: The Real Issue

**Razorpay webhooks are NOT automatically triggered from Flutter apps!**

Your Flutter app needs to **manually call your backend** after payment completion to verify and process the payment.

---

## 📱 Solution: Two-Step Approach

### Step 1: Payment in Flutter → Razorpay Checkout
### Step 2: After Success → Call Backend to Verify

---

## ✅ Updated Backend Endpoint

I've added a new endpoint for your Flutter app to call:

**Endpoint:** `POST /webhook/verify-payment`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "razorpay_order_id": "order_xxx",
  "razorpay_payment_id": "pay_xxx",
  "razorpay_signature": "signature_xxx"
}
```

**Success Response:**
```json
{
  "status": "success",
  "message": "Payment verified and subscription activated",
  "subscription_end_date": "2025-12-29",
  "subscription_status": "ACTIVE"
}
```

---

## 📝 Flutter Code Changes Needed

### Add Razorpay Package

```yaml
# pubspec.yaml
dependencies:
  razorpay_flutter: ^1.3.6
  http: ^1.1.0
```

### Complete Implementation Example

```dart
import 'package:razorpay_flutter/razorpay_flutter.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class PaymentService {
  final Razorpay _razorpay = Razorpay();
  final String baseUrl = "https://your-ngrok-url.ngrok-free.app"; // UPDATE THIS
  
  String? _orderId;
  String? _authToken;
  
  void initializeRazorpay(String authToken) {
    _authToken = authToken;
    _razorpay.on(Razorpay.EVENT_PAYMENT_SUCCESS, _handlePaymentSuccess);
    _razorpay.on(Razorpay.EVENT_PAYMENT_ERROR, _handlePaymentError);
    _razorpay.on(Razorpay.EVENT_EXTERNAL_WALLET, _handleExternalWallet);
  }
  
  // STEP 1: Create checkout and open Razorpay
  Future<void> startPayment(String userId, String planId) async {
    try {
      print("🛒 Starting payment process...");
      
      // Call your backend to create order
      final response = await http.post(
        Uri.parse('$baseUrl/webhook/checkout?userid=$userId&planId=$planId'),
        headers: {
          'Authorization': 'Bearer $_authToken',
          'Content-Type': 'application/json',
        },
      );
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        _orderId = data['razorpayOrderId'];
        final amount = data['amount'];
        
        print("✅ Order created: $_orderId");
        print("💰 Amount: ₹$amount");
        
        // Open Razorpay Checkout
        var options = {
          'key': 'rzp_test_xxxxxxxxxxxxx', // YOUR RAZORPAY KEY
          'amount': (amount * 100).toInt(), // Amount in paise
          'name': 'Your App Name',
          'order_id': _orderId,
          'description': 'Subscription Payment',
          'timeout': 300, // 5 minutes
          'prefill': {
            'contact': '9876543210',
            'email': 'user@example.com'
          }
        };
        
        _razorpay.open(options);
      } else {
        print("❌ Error creating order: ${response.body}");
        _showError("Failed to create order");
      }
    } catch (e) {
      print("❌ Exception: $e");
      _showError("Payment initialization failed: $e");
    }
  }
  
  // STEP 2: Payment Success → Verify with backend
  void _handlePaymentSuccess(PaymentSuccessResponse response) async {
    print("=====================================================");
    print("🎉 PAYMENT SUCCESS FROM RAZORPAY");
    print("=====================================================");
    print("Order ID: ${response.orderId}");
    print("Payment ID: ${response.paymentId}");
    print("Signature: ${response.signature}");
    
    try {
      // ✅ CRITICAL: Call your backend to verify payment
      print("🔍 Verifying payment with backend...");
      
      final verifyResponse = await http.post(
        Uri.parse('$baseUrl/webhook/verify-payment'),
        headers: {
          'Authorization': 'Bearer $_authToken',
          'Content-Type': 'application/json',
        },
        body: json.encode({
          'razorpay_order_id': response.orderId,
          'razorpay_payment_id': response.paymentId,
          'razorpay_signature': response.signature,
        }),
      );
      
      print("📡 Backend response status: ${verifyResponse.statusCode}");
      print("📦 Backend response body: ${verifyResponse.body}");
      
      if (verifyResponse.statusCode == 200) {
        final data = json.decode(verifyResponse.body);
        print("✅ Payment verified successfully!");
        print("📅 Subscription End Date: ${data['subscription_end_date']}");
        
        _showSuccess("Payment successful! Subscription activated.");
        
        // Navigate to success screen or update UI
        // Navigator.pushReplacement(context, SuccessScreen());
        
      } else {
        print("❌ Verification failed: ${verifyResponse.body}");
        _showError("Payment verification failed");
      }
      
    } catch (e) {
      print("❌ Error verifying payment: $e");
      _showError("Failed to verify payment: $e");
    }
  }
  
  void _handlePaymentError(PaymentFailureResponse response) {
    print("=====================================================");
    print("❌ PAYMENT FAILED");
    print("=====================================================");
    print("Code: ${response.code}");
    print("Message: ${response.message}");
    print("=====================================================");
    
    _showError("Payment failed: ${response.message}");
  }
  
  void _handleExternalWallet(ExternalWalletResponse response) {
    print("🔗 External Wallet: ${response.walletName}");
    _showError("External wallet not supported");
  }
  
  void _showSuccess(String message) {
    // Show success dialog/snackbar
    print("✅ $message");
  }
  
  void _showError(String message) {
    // Show error dialog/snackbar
    print("❌ $message");
  }
  
  void dispose() {
    _razorpay.clear();
  }
}
```

### Usage in Your Widget

```dart
class SubscriptionScreen extends StatefulWidget {
  @override
  _SubscriptionScreenState createState() => _SubscriptionScreenState();
}

class _SubscriptionScreenState extends State<SubscriptionScreen> {
  late PaymentService _paymentService;
  
  @override
  void initState() {
    super.initState();
    _paymentService = PaymentService();
    
    // Get auth token from your auth service
    final authToken = "your-jwt-token-here";
    _paymentService.initializeRazorpay(authToken);
  }
  
  @override
  void dispose() {
    _paymentService.dispose();
    super.dispose();
  }
  
  void _startPayment() {
    // Replace with actual user ID and plan ID
    final userId = "user123";
    final planId = "plan456";
    
    _paymentService.startPayment(userId, planId);
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Subscribe')),
      body: Center(
        child: ElevatedButton(
          onPressed: _startPayment,
          child: Text('Subscribe Now'),
        ),
      ),
    );
  }
}
```

---

## 🔧 Configuration Steps

### 1. Update Base URL in Flutter

```dart
final String baseUrl = "https://abc123.ngrok-free.app"; // Your ngrok URL
```

⚠️ **IMPORTANT:** Update this every time ngrok restarts (free plan)

### 2. Update Razorpay Key

```dart
'key': 'rzp_test_xxxxxxxxxxxxx', // Get from Razorpay Dashboard
```

Get this from: https://dashboard.razorpay.com/app/keys

### 3. Set Environment Variables in Backend

```powershell
$env:API_KEY="rzp_test_xxxxx"
$env:API_SECRET="xxxxxxxx"
```

---

## 🔄 Payment Flow Diagram

```
┌─────────────┐
│ Flutter App │
└──────┬──────┘
       │
       │ 1. POST /webhook/checkout
       │    (userId, planId)
       ▼
┌──────────────┐
│ Spring Boot  │
│   Backend    │ ──────► Creates Order in Database
└──────┬───────┘         Returns razorpay_order_id
       │
       │ 2. Returns order details
       ▼
┌─────────────┐
│ Flutter App │ ──────► Opens Razorpay Checkout
└──────┬──────┘
       │
       │ 3. User completes payment
       ▼
┌─────────────┐
│  Razorpay   │ ──────► Processes payment
└──────┬──────┘
       │
       │ 4. Returns payment success + signature
       ▼
┌─────────────┐
│ Flutter App │
└──────┬──────┘
       │
       │ 5. POST /webhook/verify-payment
       │    (order_id, payment_id, signature)
       ▼
┌──────────────┐
│ Spring Boot  │
│   Backend    │ ──────► Verifies signature
│              │ ──────► Updates Order status to PAID
│              │ ──────► Activates Subscription
└──────┬───────┘
       │
       │ 6. Returns success response
       ▼
┌─────────────┐
│ Flutter App │ ──────► Shows success message
│             │ ──────► Updates UI
└─────────────┘
```

---

## 🧪 Testing Checklist

- [ ] ngrok is running
- [ ] Spring Boot app is running
- [ ] Flutter app has correct ngrok URL
- [ ] Razorpay keys are configured in both Flutter and backend
- [ ] Auth token is being sent correctly
- [ ] Payment success callback is being handled
- [ ] Backend verify-payment endpoint is called after payment
- [ ] Subscription is activated in database
- [ ] UI updates to show active subscription

---

## 🐛 Debugging

### Add Logging in Flutter

```dart
// Check if backend is reachable
Future<void> testConnection() async {
  try {
    final response = await http.get(
      Uri.parse('$baseUrl/auth/health'), // Or any public endpoint
    );
    print("Backend reachable: ${response.statusCode}");
  } catch (e) {
    print("Cannot reach backend: $e");
  }
}
```

### Check Console Logs

**In Flutter:**
- Look for emoji logs: 🛒, ✅, ❌
- Check if payment success callback is triggered
- Verify backend call is being made

**In Spring Boot:**
- Look for emoji logs: 💳, ✅, ❌
- Check if verify-payment endpoint is hit
- Verify signature verification passes

---

## ❓ Common Issues

### Issue: Payment success but subscription not activated

**Solution:**
- Check if `_handlePaymentSuccess` is being called
- Verify backend URL is correct in Flutter
- Check auth token is valid
- Look at Spring Boot console for errors

### Issue: Signature verification failed

**Solution:**
- Ensure API_SECRET is same in backend
- Check signature is being sent correctly from Flutter
- Verify no extra spaces in environment variables

### Issue: Order not found

**Solution:**
- Verify order was created in Step 1
- Check order_id matches between checkout and verify
- Look at database to confirm order exists

---

## 📞 Support URLs

- **Razorpay Dashboard:** https://dashboard.razorpay.com
- **Razorpay Keys:** https://dashboard.razorpay.com/app/keys
- **Razorpay Test Cards:** https://razorpay.com/docs/payments/payments/test-card-details/

---

## 🎯 Key Takeaways

1. ✅ **Don't rely on webhooks for Flutter apps** - They're unreliable and slow
2. ✅ **Call backend immediately after payment** - Use `/verify-payment` endpoint
3. ✅ **Verify signature on backend** - Never trust client-side data
4. ✅ **Update ngrok URL** - Remember to update in Flutter when ngrok restarts
5. ✅ **Check logs on both sides** - Flutter console AND Spring Boot console

---

**Good luck! 🚀**

Remember: The payment flow is NOW:
**Flutter → Backend (create order) → Razorpay → Flutter → Backend (verify) → Success!**
