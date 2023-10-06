//
// Copyright (C) 2023 Paranoid Android
//
// SPDX-License-Identifier: Apache-2.0
//

#pragma once

#include <aidl/vendor/aospa/xiaomitouch/BnXiaomiTouch.h>

#include <mutex>
#include <thread>

namespace aidl::vendor::aospa::xiaomitouch {

class XiaomiTouch : public BnXiaomiTouch {
    public:
    XiaomiTouch(void);
    ::ndk::ScopedAStatus setModeValue(int mode, int value) override;
};

}